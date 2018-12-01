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
import src.redtalent.domain.*;
import src.redtalent.forms.EvaluationForm;
import src.redtalent.forms.RecomendationForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/recomendation")
public class RecomendationController {

    // Services ---------------------------------------------------------------
    @Autowired
    private RecomendationService recomendationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    // Constructors -----------------------------------------------------------
    public RecomendationController() {
        super();
    }

    //Lista de recomendaciones realizadas

    @RequestMapping(value="/realizedList", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam ObjectId userId) {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        User res = userService.findOne(userId.toString());

        List<Recomendation> recomendations = res.getRecomendations();

        result = new ModelAndView("recomendation/realizedList");
        result.addObject("requestURI", "recomendation/realizedList?userId=" + userId);
        result.addObject("recomendations", recomendations);
        result.addObject("userCreated",userCreated);
        result.addObject("userId", userId);

        return result;
    }

    @RequestMapping(value="/receivedList", method = RequestMethod.GET)
    public ModelAndView listReceived(@RequestParam ObjectId userId) {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        User res = userService.findOne(userId.toString());
        List<Recomendation> recomendationsReceived = res.getRecomendationsReceived();

        result = new ModelAndView("recomendation/receivedList");
        result.addObject("requestURI", "recomendation/receivedList?userId=" + userId);
        result.addObject("recomendationsReceived", recomendationsReceived);
        result.addObject("userCreated",userCreated);
        result.addObject("userId", userId);

        return result;
    }

    // Crear recomendacion a persona ----------------------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId userId, final RedirectAttributes redirectAttrs) {
        ModelAndView result = new ModelAndView("redirect:/403");
        RecomendationForm recomendationForm = new RecomendationForm();
        recomendationForm.setUserId(userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        User res = userService.findOne(userId.toString());

        if(!userCreated.equals(res)) {
            result = new ModelAndView("recomendation/create");
            result.addObject("recomendationForm", recomendationForm);
            result.addObject("requestURI", "./recomendation/create?userId=" + userId);
            result.addObject("userId", userId);
            result.addObject("userCreated", userCreated);
        }

        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid RecomendationForm recomendationForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        recomendationForm.setUserId(recomendationForm.getUserId());

        if (binding.hasErrors()) {
            result = createModelAndView(recomendationForm);
        } else {
            try {
                Assert.notNull(recomendationForm, "No puede ser nulo el formulario");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());

                Recomendation recomendation = recomendationService.create();
                recomendation.setText(recomendationForm.getText());
                Recomendation saved = this.recomendationService.save(recomendation);

                User user = userService.findOne(recomendationForm.getUserId().toString());
                List<Recomendation> recomendations = user.getRecomendationsReceived();
                recomendations.add(saved);
                user.setRecomendationsReceived(recomendations);
                userService.saveUser(user);

                List<Recomendation> recomendations1 = userCreated.getRecomendations();
                recomendations1.add(saved);
                userCreated.setRecomendations(recomendations1);
                userService.saveUser(userCreated);

                result = new ModelAndView("redirect:/user/dataUser?userId=" +recomendationForm.getUserId());

            } catch (Throwable oops) {
                result = createModelAndView(recomendationForm, "No se puede crear correctamente la recomendacion");

            }
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        if (userCreador.equals(userCreated)) {
            List<Recomendation> recomendations = user.getRecomendationsReceived();
            recomendations.remove(res);
            user.setRecomendationsReceived(recomendations);
            userService.saveUser(user);

            List<Recomendation> recomendations1 = userCreador.getRecomendations();
            recomendations1.remove(res);
            userCreador.setRecomendations(recomendations1);
            userService.saveUser(userCreador);

            recomendationService.remove(res);
        }

        result = new ModelAndView("redirect:/user/dataUser?userId=" +user.getId());
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
