package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.User;
import src.redtalent.forms.UserForm;
import src.redtalent.repositories.RoleRepository;
import src.redtalent.security.Role;
import src.redtalent.services.UserService;

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
        else if(userForm.getRole().equals("ADMIN")){
            result = createEditModelAndViewUser(userForm, "Acción denegada");
        }
        else
            try {
                User u = new User();
                u.setEmail(userForm.getEmail());
                u.setEnabled(true);
                u.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
                Role role = roleRepository.findByRole(userForm.getRole());
                Set<Role> roles = new HashSet<Role>();
                roles.add(role);
                u.setRoles(roles);
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
