package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Administrator;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.UtilidadesService;

@Controller
@RequestMapping("/admin")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private UtilidadesService utilidadesService;

    public AdministratorController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;
        result = new ModelAndView("admin/index");
        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

}
