package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Alert;
import src.redtalent.domain.Project;
import src.redtalent.domain.User;
import src.redtalent.forms.AlertForm;
import src.redtalent.services.AlertService;
import src.redtalent.services.ProjectService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
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
        result.addObject("user",user);
        result.addObject("alertas",alertasDelUsuario);

        return result;
    }

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/createAlert", method = RequestMethod.GET)
    public ModelAndView createAlert() {
        ModelAndView result;

        AlertForm alertForm;

        alertForm = new AlertForm();
        result = createEditModelAndViewAlert(alertForm);
        return result;
    }

    @RequestMapping(value = "/createAlert", method = RequestMethod.POST, params = "crearAlerta")
    public ModelAndView save(@Valid AlertForm alertForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewAlert(alertForm);
        else
            try {

                Project project = projectService.findOne(alertForm.getProject().toString());

                List<Alert> listaAlertas = project.getAlerts();
                Alert newAlert = alertService.create();
                String alerta = "[code:nc]"+alertForm.getText()+".";
                newAlert.setText(alerta);
                Alert alertSave = alertService.save(newAlert);
                listaAlertas.add(alertSave);
                project.setAlerts(listaAlertas);

                Project savee = projectService.save(project);
                projectService.saveAll(savee);

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = createEditModelAndViewAlert(alertForm, "ERROR AL CREAR LA NOTIFICACION");
            }

        return result;
    }

    protected ModelAndView createEditModelAndViewAlert(AlertForm alertForm) {
        ModelAndView result;
        result = createEditModelAndViewAlert(alertForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewAlert(AlertForm alertForm, String message) {
        ModelAndView result;

        result = new ModelAndView("user/alert/createAlert");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        result.addObject("alertForm", alertForm);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",user.getProjects());
        result.addObject("message", message);

        return result;
    }

}
