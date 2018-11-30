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
            result = createEditModelAndViewUser(userForm, "Por favor, acepte las condiciones de uso.");
        else if(utilidadesService.allEmails().contains(userForm.getEmail())){
            result = createEditModelAndViewUser(userForm, "El email especificado está en uso.");
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

                if(userForm.getImage().isEmpty()){
                    u.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAPFBMVEXMzMzl5eXg4ODv7+/R0dHU1NTb29vt7e3Z2dno6Ojx8fHi4uLs7Ozq6urj4+PPz8/e3t7W1tby8vL09PQMHpbdAAADDUlEQVR42u3dWZLsKAwFUDEYD/UAw93/XvuTj46OLrsgJdXT2cGNRAxOEZAxxhhjjDHGGGOMMcYYYwy/2u6ScweAnnO5W9UY4ig7/mUvR1WVImX8p5y0ZEkn/seZSLzqA74h+EqSRYdvc5Gkqg6PuEoi+Y6Huid5WsALoZEsteClUkmQbcdr+0ZiJPxIIiEKfqiQCA4/5pTnGJzyHIOTkUN/koRpEjHaOqbpG/HZMdFObG5MdROT2jFVr8TDYzJPPDom68xTr/YpOGO6TAwiFoi6597h5htZ+scWlqCPa1iica2G+tfEE0ucnBtf3VtgLMK0HOpfEhsWaRbkHY9FvAWxIBbEglgQC2JBLIgFsd2vBbEgXEHs44MFsSAWxIL8FUEylsj0WWnHInti79JS2LlVsFRR/pfCEHWfDgeve8IaMldXkNYuISxnQaQFsWJ/5sRiJ0enluaurQ2LbfQhAUsF+hTPsEPRuGuMdh55rF5Y5qosXdjqu7Gd8oE1fGGJL/q4I2C6cEi9vq7kgvsXw7BSUPKO+NSAaUIlRg3TNLviKmqzclVi5hm27oJPWYH4NeWVPmSG7z9Sz4uRRPDKK30Iuit9aMorffDKB9bwB6/9IUnihZeuSKJsvN95+T8QJRIn8eXgX00CSVTYmjX4630jkS48dJFMjumczj9vJZIp8hxD+IvkIqkyywGXfzfvSaoDjxwkVcMjjaSqeKSSWHiELIisnXwgoeK945H9jiTO5ne8sPtNVIoS8Foom7AUqrMcrmOK7g79KUYW1hSqs2ylY5FeNh3VzV/7Y9WbgHutTCem4Xs2ZnMdH9XdRtPVeweD/a4MP4b4nyVlsMqJJqi+g133ddIzSPxcpPfaCUHO9jZGhjC5ccXgjxIzhMqRrcear1/77hCt3/QdbYd4e2MYVTzjqwUoEdqkF9v4lfrgxTZG71+Jax3K9Paxq8QM15MdVHJrcvAncVDLzc/BnyRBtTQ6xpTbxtU1xcaFuQL1yuiyUi6OGUs3N34Q5SJ5/AqeAn6FQPglLIg0/wCowqSZm6rQPwAAAABJRU5ErkJggg==");
                }else {
                    u.setImage(userForm.getImage());
                }

                Account save = accountRepository.save(a);
                u.setAccount(save);
                u.setFullname(userForm.getFullname());
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
