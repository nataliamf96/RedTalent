package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.*;
import src.redtalent.forms.ProjectForm;
import src.redtalent.forms.UserForm;
import src.redtalent.security.Role;
import src.redtalent.services.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AlertService alertService;

    public ProjectController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        result = new ModelAndView("project/index");
        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/projects")
    public ModelAndView projects() {
        ModelAndView result;

        result = new ModelAndView("project/projects");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",projectService.findAll());
        return result;
    }

    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public ModelAndView projectDataList2(@RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("project/project");

        Project project = projectService.findOne(projectId.toString());
        Team team = teamService.teamByProjectId(project);

        result.addObject("user",userService.findUserByProjectsContains(project));
        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("comments",project.getComments());

        return result;
    }

    @RequestMapping(value = "/projectData", method = RequestMethod.GET)
    public ModelAndView projectDataList(@RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("project/projectData");

        Project project = projectService.findOne(projectId.toString());
        Team team = teamService.teamByProjectId(project);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        Boolean yaEresDelEquipo = utilidadesService.estaEnElEquipo(user,team);

        List<User> usuariosTeam = utilidadesService.usuariosDelEquipo(team);

        Boolean valora = true;
        for(Evaluation e:project.getEvaluations()){
            if(user.getEvaluations().contains(e)){
                valora = false;
            }
        }

        result.addObject("tieneSolicitudEnviada",utilidadesService.tieneSolicitudEnviada(user,team));
        result.addObject("yaEresDelEquipo",yaEresDelEquipo);
        result.addObject("user",user);
        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("usuariosTeam",usuariosTeam);
        result.addObject("users",userService.findAll());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("comments",project.getComments());
        result.addObject("valora", valora);

        return result;
    }

    @RequestMapping(value = "/allProjectData", method = RequestMethod.GET)
    public ModelAndView allProjectData(@RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("project/allProjectData");

        Project project = projectService.findOne(projectId.toString());
        Team team = teamService.teamByProjectId(project);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userConnect = utilidadesService.userConectado(authentication.getName());

        result.addObject("user",userService.findUserByProjectsContains(project));
        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("comments",project.getComments());
        result.addObject("userConnect",userConnect);

        return result;
    }

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/createProject", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView result;
        ProjectForm projectForm;

        projectForm = new ProjectForm();
        result = createEditModelAndViewProject(projectForm);
        result.addObject("categories", categoryService.findAll());
        return result;
    }

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/updateProject", method = RequestMethod.GET)
    public ModelAndView updateProject(@RequestParam String projectId) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userConnect = utilidadesService.userConectado(authentication.getName());

        try{
            Assert.notNull(projectService.findOne(projectId),"El proyecto no existe");
            Assert.isTrue(userConnect.getProjects().contains(projectService.findOne(projectId.toString())),"El proyecto es de otro usuario.");
            Project project = projectService.findOne(projectId);
            ProjectForm projectForm;
            projectForm = new ProjectForm();
            result = updateEditModelAndViewProject(projectForm);
            result = new ModelAndView("project/updateProject");
            result.addObject("projectForm",project);
            result.addObject("categories", categoryService.findAll());
            result.addObject("projectId",project.getId());
        }catch (Throwable oops) {
            result = new ModelAndView("redirect:/403");
        }


        return result;
    }

    @RequestMapping(value = "/updateProject", method = RequestMethod.POST, params = "updateProject")
    public ModelAndView updateProject(@Valid ProjectForm projectForm, @RequestParam String projectId, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = updateEditModelAndViewProject(projectForm);
        else
            try {
                Project project = projectService.findOne(projectId);
                project.setDescription(projectForm.getDescription());
                project.setName(projectForm.getName());
                project.setImage(projectForm.getImage());
                project.setRequiredProfiles(projectForm.getRequiredProfiles());
                project.setPrivado(projectForm.getPrivado());
                Category cat = categoryService.findOne(projectForm.getCategory().toString());
                project.setCategorie(cat);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());

                Project savee = projectService.save(project);

                Set<Project> pp = new HashSet<Project>();
                pp.addAll(user.getProjects());
                pp.remove(projectService.findOne(projectId));
                pp.add(savee);
                user.setProjects(pp);

                Team tt = null;
                for(Team team:user.getTeams()){
                    if(team.getProjects().contains(savee)){
                        tt = team;
                        List<Project> projectsS = new ArrayList<Project>();
                        projectsS.addAll(tt.getProjects());
                        projectsS.remove(projectService.findOne(projectId));
                        projectsS.add(savee);
                        tt.setProjects(projectsS);
                        Team ttSave = teamService.save(tt);

                        Set<Team> listaEquiposUser = user.getTeams();
                        listaEquiposUser.remove(tt);
                        listaEquiposUser.add(ttSave);
                        user.setTeams(listaEquiposUser);
                    }
                }


                userService.saveUser(user);

                Team team = null;
                for(Team teamm:teamService.findAll()){
                        if(teamm.getProjects().contains(savee)){
                            team = teamm;
                        }
                }

                if(team != null){
                    List<Project> ppp = new ArrayList<Project>();
                    ppp.addAll(team.getProjects());
                    ppp.remove(projectService.findOne(projectId));
                    ppp.add(savee);
                    team.setProjects(ppp);
                    teamService.save(team);
                }

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = updateEditModelAndViewProject(projectForm, "ERROR AL ACTUALIZAR EL PROYECTO");
            }

        return result;
    }

    @RequestMapping(value = "/cerrarProyecto", method = RequestMethod.GET)
    public ModelAndView cerrarProyecto(@RequestParam String projectId) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        try{
            Assert.isTrue(user.getProjects().contains(projectService.findOne(projectId)),"El usuario creador no es el conectado.");

            Project project = projectService.findOne(projectId);
            project.setEstado(true);

            List<Alert> listaAlertas = project.getAlerts();
            Alert newAlert = alertService.create();
            String alerta = "[code:cp]El proyecto " +project.getName()+ " ha sido cerrado.";
            newAlert.setText(alerta);
            Alert alertSave = alertService.save(newAlert);
            listaAlertas.add(alertSave);
            project.setAlerts(listaAlertas);

            Project savee = projectService.save(project);

            projectService.saveAll(savee);

            result = new ModelAndView("redirect:/user/index");
            result.addObject("auth",utilidadesService.actorConectado());
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/403");
        }

        return result;
    }

    @RequestMapping(value = "/borrarProyecto", method = RequestMethod.GET)
    public ModelAndView borrarProyecto(@RequestParam String projectId) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        try{
            Assert.isTrue(user.getProjects().contains(projectService.findOne(projectId)),"El usuario creador no es el conectado.");

            Project project = projectService.findOne(projectId);

            Team team = teamService.teamByProjectId(project);
            team.setClosed(true);
            Team teamSave = teamService.save(team);

            User userCambiar = user;
            Set<Team> teamsCambiar = userCambiar.getTeams();
            teamsCambiar.remove(team);
            teamsCambiar.add(teamSave);
            userCambiar.setTeams(teamsCambiar);
            userService.saveUser(userCambiar);

            project.setCerrado(true);

            List<Alert> listaAlertas = project.getAlerts();
            Alert newAlert = alertService.create();
            String alerta = "[code:bp]El proyecto " +project.getName()+ " ha sido borrado.";
            newAlert.setText(alerta);
            Alert alertSave = alertService.save(newAlert);
            listaAlertas.add(alertSave);
            project.setAlerts(listaAlertas);

            Project savee = projectService.save(project);

            projectService.saveAll(savee);

            result = new ModelAndView("redirect:/user/index");
            result.addObject("auth",utilidadesService.actorConectado());
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/403");
        }

        return result;
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST, params = "saveProject")
    public ModelAndView save(@Valid ProjectForm projectForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewProject(projectForm);
        else
            try {
                Project project = projectService.create();
                project.setDescription(projectForm.getDescription());
                project.setName(projectForm.getName());
                if(projectForm.getImage().isEmpty()){
                   project.setImage("data:image/png;base64,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");
                }else{
                    project.setImage(projectForm.getImage());
                }

                project.setRequiredProfiles(projectForm.getRequiredProfiles());
                project.setPrivado(projectForm.getPrivado());
                Category cat = categoryService.findOne(projectForm.getCategory().toString());
                project.setCategorie(cat);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());

                Project savee = projectService.save(project);

                Set<Project> pp = new HashSet<Project>();
                pp.addAll(user.getProjects());
                pp.add(savee);
                user.setProjects(pp);
                userService.saveUser(user);


                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = createEditModelAndViewProject(projectForm, "ERROR AL CREAR EL PROYECTO");
            }

        return result;
    }



    protected ModelAndView updateEditModelAndViewProject(ProjectForm projectForm) {
        ModelAndView result;
        result = updateEditModelAndViewProject(projectForm, null);
        return result;
    }

    protected ModelAndView updateEditModelAndViewProject(ProjectForm projectForm, String message) {
        ModelAndView result;

        result = new ModelAndView("project/updateProject");
        result.addObject("projectForm", projectForm);
        result.addObject("categories", categoryService.findAll());
        result.addObject("message", message);

        return result;
    }

    protected ModelAndView createEditModelAndViewProject(ProjectForm projectForm) {
        ModelAndView result;
        result = createEditModelAndViewProject(projectForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewProject(ProjectForm projectForm, String message) {
        ModelAndView result;

        result = new ModelAndView("project/createProject");
        result.addObject("projectForm", projectForm);
        result.addObject("categories", categoryService.findAll());
        result.addObject("message", message);

        return result;
    }

}