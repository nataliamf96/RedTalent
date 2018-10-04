package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Project;
import src.redtalent.services.ProjectService;

import java.util.Collection;

@Controller
@RequestMapping("/")
public class HomeController extends AbstractController {

    @Autowired
    private ProjectService projectService;

    public HomeController(){
        super();
    }

    @GetMapping("/")
    public ModelAndView index(Model model) {
        ModelAndView result;
        Collection<Project> projects = projectService.findAll();

        result = new ModelAndView("welcome/index");
        result.addObject("projects", projects);
        return result;
    }

}
