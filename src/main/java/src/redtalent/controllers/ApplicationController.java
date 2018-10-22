package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.*;
import src.redtalent.services.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/application")
public class ApplicationController{

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UtilidadesService utilidadesService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    public ApplicationController(){
        super();
    }

    // Listing ----------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        result = new ModelAndView("application/list");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        result.addObject("applications", user.getApplications());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("requestURI", "application/list");

        return result;
    }

    @RequestMapping(value = "/crearAplicacionTeam", method = RequestMethod.GET)
    public ModelAndView crearAplicacion(@RequestParam ObjectId teamId){
        ModelAndView result;
        result = new ModelAndView("application/list");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        Application application = applicationService.create();
        Application savee = applicationService.save(application);

        Set<Application> applicationsUser = user.getApplications();
        applicationsUser.add(savee);
        user.setApplications(applicationsUser);
        userService.saveUser(user);

        Team team = teamService.findOne(teamId.toString());
        List<Application> applicationsTeam = team.getApplications();
        applicationsTeam.add(savee);
        team.setApplications(applicationsTeam);
        teamService.save(team);

        return result;
    }

    @RequestMapping(value = "/crearAplicacionProyecto", method = RequestMethod.GET)
    public ModelAndView crearAplicacionProyecto(@RequestParam ObjectId projectId){
        ModelAndView result;
        result = new ModelAndView("application/list");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        Application application = applicationService.create();
        Application savee = applicationService.save(application);

        Set<Application> applicationsUser = user.getApplications();
        applicationsUser.add(savee);
        user.setApplications(applicationsUser);
        userService.saveUser(user);

        Project project = projectService.findOne(projectId.toString());

        Team team = teamService.teamByProjectId(project);
        List<Application> applicationsTeam = team.getApplications();
        applicationsTeam.add(savee);
        team.setApplications(applicationsTeam);
        teamService.save(team);

        return result;
    }

}
