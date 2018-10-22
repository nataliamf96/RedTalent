package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Administrator;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.ProjectService;
import src.redtalent.services.UtilidadesService;

@Controller
@RequestMapping("/admin")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private UtilidadesService utilidadesService;
    @Autowired
    private ProjectService projectService;

    public AdministratorController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("admin/index");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",projectService.findAllByPrivadoFalse());
        result.addObject("admin",utilidadesService.adminConectado(authentication.getName()));
        return result;
    }

}
