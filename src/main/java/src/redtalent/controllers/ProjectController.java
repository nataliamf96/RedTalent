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
import src.redtalent.services.UtilidadesService;

@Controller
@RequestMapping("/user/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private TeamService teamService;

    public ProjectController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        result = new ModelAndView("user/projects");
        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/projectData", method = RequestMethod.GET)
    public ModelAndView projectDataList(@RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("project/projectData");

        Project project = projectService.findOne(projectId.toString());
        User user = project.getUsers().get(0);
        Team team = teamService.teamByProjectId(project);

        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",user.getFullname());
        result.addObject("comments",project.getComments());

        return result;
    }

}
