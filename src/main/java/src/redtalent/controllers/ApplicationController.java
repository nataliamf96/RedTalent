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

import java.util.*;

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
        List<Project> projects = new ArrayList<Project>();
        user.getApplications().stream().forEach(x->projects.add(teamService.findTeamByApplicationsContaining(x).getProjects().get(0)));
        List<Team> teams = new ArrayList<Team>();
        user.getApplications().stream().forEach(x->teams.add(teamService.findTeamByApplicationsContaining(x)));

        result.addObject("teams", teams);
        result.addObject("projects", projects);
        result.addObject("applications", user.getApplications());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("requestURI", "application/list");

        return result;
    }

    @RequestMapping(value = "/solicitudesEquipo", method = RequestMethod.GET)
    public ModelAndView solicitudesEquipo(@RequestParam ObjectId teamId) {
        ModelAndView result;
        result = new ModelAndView("application/solicitudesEquipo");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Team team = teamService.findOne(teamId.toString());
        List<Project> projects = new ArrayList<Project>();
        team.getApplications().stream().forEach(x->projects.add(teamService.findTeamByApplicationsContaining(x).getProjects().get(0)));
        List<Team> teams = new ArrayList<Team>();
        team.getApplications().stream().forEach(x->teams.add(teamService.findTeamByApplicationsContaining(x)));
        List<User> users = new ArrayList<User>();
        team.getApplications().stream().forEach(x->users.add(userService.findUserByApplicationsContains(x)));

        result.addObject("users", users);
        result.addObject("teams", teams);
        result.addObject("projects", projects);
        result.addObject("applications", team.getApplications());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("requestURI", "application/list");

        return result;
    }

    @RequestMapping(value = "/crearAplicacionTeam", method = RequestMethod.GET)
    public ModelAndView crearAplicacion(@RequestParam ObjectId teamId){
        ModelAndView result;
        result = new ModelAndView("application/crearAplicacionTeam");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        Application application = applicationService.create();
        Application savee = applicationService.save(application);

        Set<Application> applicationsUser = new HashSet<Application>();
        applicationsUser.addAll(user.getApplications());
        applicationsUser.add(savee);
        user.setApplications(applicationsUser);
        userService.saveUser(user);

        Team team = teamService.findOne(teamId.toString());
        List<Application> applicationsTeam = new ArrayList<Application>();
        applicationsTeam.addAll(team.getApplications());
        applicationsTeam.add(savee);
        team.setApplications(applicationsTeam);
        Team saTeam = teamService.save(team);

        //Usuario creador del Team -> Debe de guardar también las aplications nuevas dentro del team

        User u = userService.findUserByTeamsConstains(team);
        Set<Team> tt = new HashSet<Team>();
        tt.addAll(u.getTeams());
        tt.remove(team);
        tt.add(saTeam);
        u.setTeams(tt);
        userService.saveUser(u);



        result = new ModelAndView("redirect:/application/list");

        return result;
    }

    @RequestMapping(value = "/crearAplicacionProyecto", method = RequestMethod.GET)
    public ModelAndView crearAplicacionProyecto(@RequestParam ObjectId projectId){
        ModelAndView result;
        result = new ModelAndView("application/crearAplicacionProyecto");

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
        Team saTeam = teamService.save(team);

        //Usuario creador del Team -> Debe de guardar también las aplications nuevas dentro del team

        User u = userService.findUserByProjectsContains(project);
        Set<Team> tt = new HashSet<Team>();
        tt.addAll(u.getTeams());
        tt.remove(team);
        tt.add(saTeam);
        u.setTeams(tt);
        userService.saveUser(u);

        result = new ModelAndView("redirect:/application/list");

        return result;
    }

    @RequestMapping(value = "/aplicar", method = RequestMethod.GET)
    public ModelAndView aplicar(@RequestParam ObjectId applicationId, @RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("application/aplicar");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Usuario creador Team
        User user = utilidadesService.userConectado(authentication.getName());

        Application a = applicationService.findOne(applicationId.toString());
        a.setStatus("ACCEPTED");
        Application savee = applicationService.save(a);

        //Usuario creador Application
        User userCrea = userService.findUserByApplicationsContains(a);
        Set<Application> appUserCrea = new HashSet<Application>();
        appUserCrea.addAll(userCrea.getApplications());
        appUserCrea.remove(a);
        appUserCrea.add(savee);
        userCrea.setApplications(appUserCrea);
        userService.saveUser(userCrea);

        Project p = projectService.findOne(projectId.toString());
        Team t = teamService.teamByProjectId(p);
        List<Application> appUser = new ArrayList<Application>();
        appUser.addAll(t.getApplications());
        appUser.remove(a);
        appUser.add(savee);
        t.setApplications(appUser);
        Team saveTeam = teamService.save(t);
        Set<Team> teams = new HashSet<Team>();
        teams.addAll(user.getTeams());
        teams.remove(t);
        teams.add(saveTeam);
        user.setTeams(teams);
        userService.saveUser(user);

        result = new ModelAndView("redirect:/user/index");

        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

}
