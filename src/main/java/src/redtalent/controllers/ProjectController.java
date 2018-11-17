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
import src.redtalent.domain.Category;
import src.redtalent.domain.Project;
import src.redtalent.domain.Team;
import src.redtalent.domain.User;
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

        result.addObject("tieneSolicitudEnviada",utilidadesService.tieneSolicitudEnviada(user,team));
        result.addObject("yaEresDelEquipo",yaEresDelEquipo);
        result.addObject("user",userService.findUserByProjectsContains(project));
        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("comments",project.getComments());

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
        Project project = projectService.findOne(projectId);
        ProjectForm projectForm;
        projectForm = new ProjectForm();
        result = updateEditModelAndViewProject(projectForm);
        result = new ModelAndView("project/updateProject");
        result.addObject("projectForm",project);
        result.addObject("categories", categoryService.findAll());
        result.addObject("projectId",project.getId());
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
                project.setAttachedFiles(projectForm.getAttachedFiles());
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
                project.setImage(projectForm.getImage());
                project.setAttachedFiles(projectForm.getAttachedFiles());
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