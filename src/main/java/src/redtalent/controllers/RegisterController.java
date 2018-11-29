package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Account;
import src.redtalent.domain.Tag;
import src.redtalent.domain.User;
import src.redtalent.forms.UserForm;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.repositories.RoleRepository;
import src.redtalent.security.Role;
import src.redtalent.services.TagService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/registro")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TagService tagService;

    public RegisterController(){
        super();
    }

    // RegisterUser ---------------------------------------------------------------
    //GET--------------------------------------------------------------
    @RequestMapping(value = "/registerUser", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView result;
        UserForm userForm;

        userForm = new UserForm();
        result = createEditModelAndViewUser(userForm);

        return result;
    }

    //POST-----------------------------------------------------------------
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST, params = "saveUser")
    public ModelAndView save(@Valid UserForm userForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewUser(userForm);
        else if (!userForm.getTerms())
            result = createEditModelAndViewUser(userForm, "Debe de Aceptar los Términos");
        else if(utilidadesService.allEmails().contains(userForm.getEmail())){
            result = createEditModelAndViewUser(userForm, "Email Existente");
        }
        else
            try {
                User u = userService.create();
                Account a = new Account();
                a.setEmail(userForm.getEmail());
                a.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
                Role role = roleRepository.findByRole("USER");
                Set<Role> roles = new HashSet<Role>();
                roles.add(role);
                a.setRoles(roles);
                a.setEnabled(false);

                Account save = accountRepository.save(a);
                u.setAccount(save);
                u.setFullname(userForm.getFullname());
                u.setImage(userForm.getImage());
                userService.saveUser(u);

                result = new ModelAndView("redirect:/login");

            } catch (Throwable oops) {
                result = createEditModelAndViewUser(userForm, "ERROR EN EL REGISTRO");
            }

        return result;
    }


    protected ModelAndView createEditModelAndViewUser(UserForm userForm) {
        ModelAndView result;
        result = createEditModelAndViewUser(userForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewUser(UserForm userForm, String message) {
        ModelAndView result;

        result = new ModelAndView("registro/registerUser");
        result.addObject("userForm", userForm);
        result.addObject("message", message);

        return result;
    }


}
