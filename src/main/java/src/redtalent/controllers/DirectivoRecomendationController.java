package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Recomendation;
import src.redtalent.domain.User;
import src.redtalent.forms.RecomendationForm;
import src.redtalent.services.RecomendationService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/directivo/recomendation")
public class DirectivoRecomendationController {

    // Services ---------------------------------------------------------------
    @Autowired
    private RecomendationService recomendationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    // Constructors -----------------------------------------------------------
    public DirectivoRecomendationController() {
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

        result = new ModelAndView("directivo/recomendation/list");
        result.addObject("requestURI", "directivo/recomendation/list");
        result.addObject("recomendations", recomendations);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }
}
