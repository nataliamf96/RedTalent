package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Project;
import src.redtalent.domain.Team;
import src.redtalent.domain.User;
import src.redtalent.services.ProjectService;
import src.redtalent.services.TeamService;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeamService teamService;

    public ProjectController(){
        super();
    }

    // Listing ----------------------------------------------------------------
    @RequestMapping(value = "/listProjects", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        result = new ModelAndView("project/listProjects");

        result.addObject("requestURI", "project/listProjects.html");
        result.addObject("projects",projectService.findAll());

        return result;
    }

    @RequestMapping(value = "/projectData", method = RequestMethod.GET)
    public ModelAndView projectDataList(@RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("project/projectData");
        Project project = projectService.findOne(projectId.toString());
        User user = project.getUsers().get(0);
        Team team = teamService.teamByProjectId(project);

        result.addObject("requestURI", "project/projectData.html");
        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("user",user);
        result.addObject("comments",project.getComments());

        return result;
    }



}
