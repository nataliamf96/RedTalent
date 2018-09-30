package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.User;
import src.redtalent.forms.UserForm;
import src.redtalent.security.Authority;
import src.redtalent.security.UserAccount;
import src.redtalent.services.ActorService;
import src.redtalent.services.CompanyService;
import src.redtalent.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/security")
public class RegistrationController {

    @Autowired
    private ActorService actorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    public RegistrationController(){
        super();
    }

    // RegisterUser ---------------------------------------------------------------
    //GET--------------------------------------------------------------
    @RequestMapping(value = "/registerStudent", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView result;
        UserForm userForm;
        userForm = new UserForm();
        result = createModelAndViewStudent(userForm);
        return result;
    }

    //POST-----------------------------------------------------------------
    @RequestMapping(value = "/registerStudent", method = RequestMethod.POST, params = "saveStudent")
    public ModelAndView save(@Valid UserForm userForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createModelAndViewStudent(userForm);
        else
            try {
                User u =  userService.create();
                u.setName(userForm.getName());
                u.setRole("STUDENT");
                u.setEmail(userForm.getEmail());
                u.setSurname(userForm.getSurname());
                // UserAccount --------------------------------------------
                Authority auth = new Authority();
                auth.setAuthority(Authority.USER);
                UserAccount userAccount = new UserAccount();
                userAccount.setUsername(userForm.getUsername());
                Md5PasswordEncoder encoder = new Md5PasswordEncoder();
                userAccount.setPassword(encoder.encodePassword(userForm.getPassword(), null));
                userAccount.getAuthorities().add(auth);
                u.setUserAccount(userAccount);
                userService.save(u);

                result = new ModelAndView("redirect:/security/login.html");

            } catch (Throwable oops) {
                result = createModelAndViewStudent(userForm, "Existe un error al crear el usuario estudiante.");
            }
        return result;
    }

    /*
    FALLOS ============================================================================================================
     */

    protected ModelAndView createModelAndViewStudent(UserForm userForm) {
        ModelAndView result;
        result = createModelAndViewStudent(userForm, null);
        return result;
    }

    protected ModelAndView createModelAndViewStudent(UserForm userForm, String message) {
        ModelAndView result;

        result = new ModelAndView("security/registerStudent");
        result.addObject("userForm", userForm);
        result.addObject("message", message);

        return result;
    }

}
