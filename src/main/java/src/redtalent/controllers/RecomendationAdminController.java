package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.redtalent.domain.Recomendation;
import src.redtalent.domain.User;
import src.redtalent.forms.RecomendationForm;
import src.redtalent.services.RecomendationService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/recomendation")
public class RecomendationAdminController {

    // Services ---------------------------------------------------------------
    @Autowired
    private RecomendationService recomendationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    // Constructors -----------------------------------------------------------
    public RecomendationAdminController() {
        super();
    }


    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        List<Recomendation> recomendations = new ArrayList<>();

        for(User u : userService.findAll()){
            recomendations.addAll(u.getRecomendationsReceived());
        }

        result = new ModelAndView("admin/recomendation/list");
        result.addObject("requestURI", "admin/recomendation/list");
        result.addObject("recomendations", recomendations);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    // Delete recomendation ----------------------------

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView deleteRecomendation(@RequestParam ObjectId recomendationId) {
        ModelAndView result;
        Assert.notNull(recomendationService.findOne(recomendationId.toString()), "La id no puede ser nula");

        Recomendation res = recomendationService.findOne(recomendationId.toString());
        User userCreador = userService.findUserByRecomendationsContains(res);
        User user = userService.findUserByRecomendationsReceivedContains(res);

            List<Recomendation> recomendations = user.getRecomendationsReceived();
            recomendations.remove(res);
            user.setRecomendationsReceived(recomendations);
            userService.saveUser(user);

            List<Recomendation> recomendations1 = userCreador.getRecomendations();
            recomendations1.remove(res);
            userCreador.setRecomendations(recomendations1);
            userService.saveUser(userCreador);

            recomendationService.remove(res);

        result = new ModelAndView("redirect:/admin/recomendation/list");
        return result;
    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(RecomendationForm recomendationForm) {
        ModelAndView result;
        result = createModelAndView(recomendationForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(RecomendationForm recomendationForm, String message) {
        ModelAndView result;

        result = new ModelAndView("recomendation/create");
        result.addObject("recomendationForm", recomendationForm);
        result.addObject("userId", recomendationForm.getUserId());
        result.addObject("message", message);
        return result;
    }

}
