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
    @Autowired
    private AlertService alertService;

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
    public ModelAndView solicitudesEquipo() {
        ModelAndView result;
        result = new ModelAndView("application/solicitudesEquipo");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        List<Project> projects = new ArrayList<Project>();
        List<Team> teams = new ArrayList<Team>();
        List<User> users = new ArrayList<User>();
        List<Application> applications = new ArrayList<Application>();
        for(Team team : user.getTeams()){
        team.getApplications().stream().forEach(x->projects.add(teamService.findTeamByApplicationsContaining(x).getProjects().get(0)));
        team.getApplications().stream().forEach(x->teams.add(teamService.findTeamByApplicationsContaining(x)));
        team.getApplications().stream().forEach(x->users.add(userService.findUserByApplicationsContains(x)));
        team.getApplications().stream().forEach(x->applications.add(x));
        }
        result.addObject("users", users);
        result.addObject("teams", teams);
        result.addObject("projects", projects);
        result.addObject("applications", applications);
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

        if(projectService.findOne(projectId.toString()).getEstado()==true || projectService.findOne(projectId.toString()).getCerrado()==true){
            return new ModelAndView("redirect:/403");
        }else{
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
        }

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
        a.setStatus("ACEPTADO");
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

        List<Alert> listaAlertas = p.getAlerts();
        Alert newAlert = alertService.create();
        String alerta = "[code:as]El Usuario "+userCrea.getFullname()+" se ha unido al Equipo "+ t.getName() + " para trabajar en el proyecto "+p.getName()+".";
        newAlert.setText(alerta);
        Alert alertSave = alertService.save(newAlert);
        listaAlertas.add(alertSave);
        p.setAlerts(listaAlertas);
        Project psave = projectService.save(p);
        projectService.saveAll(psave);

        result = new ModelAndView("redirect:/user/index");

        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

    @RequestMapping(value = "/noaplicar", method = RequestMethod.GET)
    public ModelAndView noaplicar(@RequestParam ObjectId applicationId, @RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("application/noaplicar");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Usuario creador Team
        User user = utilidadesService.userConectado(authentication.getName());

        Application a = applicationService.findOne(applicationId.toString());
        a.setStatus("DENIED");
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
