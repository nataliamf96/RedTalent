package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Alert;
import src.redtalent.domain.User;
import src.redtalent.services.AlertService;
import src.redtalent.services.ProjectService;
import src.redtalent.services.UtilidadesService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UtilidadesService utilidadesService;

    // Listing ----------------------------------------------------------------
    @RequestMapping(value = "/listAlerts", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        result = new ModelAndView("user/alert/listAlerts");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        List<Alert> alertasDelUsuario = new ArrayList<Alert>();
        if(user.getProjects().size()!=0){
            user.getProjects().stream().forEach(x-> alertasDelUsuario.addAll(x.getAlerts()));
        }
        if(utilidadesService.todosLosProyectosEnLosQueEstoy(user).size()!=0){
            utilidadesService.todosLosProyectosEnLosQueEstoy(user).stream().forEach(x-> alertasDelUsuario.addAll(x.getAlerts()));
        }
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("alertas",alertasDelUsuario);

        return result;
    }

}
