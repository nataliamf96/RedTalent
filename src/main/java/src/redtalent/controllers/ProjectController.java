package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.services.ProjectService;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    public ProjectController(){
        super();
    }

    // Listing ----------------------------------------------------------------
    @RequestMapping(value = "/listProjects", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        result = new ModelAndView("project/listProjects");

        result.addObject("requestURI", "project/listProjects.html");

        return result;
    }

}
