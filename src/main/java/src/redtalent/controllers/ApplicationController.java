package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.*;
import src.redtalent.services.ApplicationService;

import java.util.Collection;

@Controller
@RequestMapping("/application")
public class ApplicationController{

    @Autowired
    ApplicationService applicationService;

    public ApplicationController(){
        super();
    }

    // Listing ----------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        result = new ModelAndView("application/list");

        Collection<Application> applications = applicationService.findAll();

        result.addObject("applications", applications);
        result.addObject("requestURI", "application/list.html");

        return result;
    }
}
