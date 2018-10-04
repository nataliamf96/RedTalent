package src.redtalent.security;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.controllers.AbstractController;

import javax.validation.Valid;

@Controller
@RequestMapping("/security")
public class LoginController extends AbstractController {

    public LoginController() {
        super();
    }

    @RequestMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute final Credentials credentials, final BindingResult bindingResult, @RequestParam(required = false) final boolean showError) {
        Assert.notNull(credentials,"credentials null");
        Assert.notNull(bindingResult,"message null");

        ModelAndView result;

        result = new ModelAndView("security/login");
        result.addObject("credentials", credentials);
        result.addObject("showError", showError);

        return result;
    }


    // LoginFailure -----------------------------------------------------------

    @RequestMapping("/loginFailure")
    public ModelAndView failure() {
        ModelAndView result;

        result = new ModelAndView("redirect:login.html?showError=true");

        return result;
    }


}
