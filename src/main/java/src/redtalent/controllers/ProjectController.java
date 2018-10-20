package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Project;
import src.redtalent.domain.Team;
import src.redtalent.domain.User;
import src.redtalent.forms.ProjectForm;
import src.redtalent.forms.UserForm;
import src.redtalent.security.Role;
import src.redtalent.services.ProjectService;
import src.redtalent.services.TeamService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;
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

    @RequestMapping(value = "/projectData", method = RequestMethod.GET)
    public ModelAndView projectDataList(@RequestParam ObjectId projectId) {
        ModelAndView result;
        result = new ModelAndView("project/projectData");

        Project project = projectService.findOne(projectId.toString());
        Team team = teamService.teamByProjectId(project);

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

        result.addObject("user",userService.findUserByProjectsContains(project));
        result.addObject("project",project);
        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("comments",project.getComments());

        return result;
    }

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/createProject", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView result;
        ProjectForm projectForm;

        projectForm = new ProjectForm();
        result = createEditModelAndViewProject(projectForm);

        return result;
    }

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/updateProject", method = RequestMethod.GET)
    public ModelAndView updateProject(@RequestParam String projectId) {
        ModelAndView result;
        Project project = projectService.findOne(projectId);
        result = new ModelAndView("project/updateProject");
        result.addObject("project",project);
        return result;
    }

    @RequestMapping(value = "/updateProject", method = RequestMethod.POST, params = "updateProject")
    public ModelAndView updateProject(@Valid ProjectForm projectForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = updateEditModelAndViewProject(projectForm);
        else if(projectForm.getTerms() == false){
            result = updateEditModelAndViewProject(projectForm,"Acepte los Términos");
        } else
            try {
                Project project = projectService.findOne(projectForm.getId().toString());
                project.setComplexity(projectForm.getComplexity());
                project.setDescription(projectForm.getDescription());
                project.setName(projectForm.getName());
                project.setImage(projectForm.getImage());
                project.setMaxParticipants(projectForm.getMaxParticipants());
                project.setFinishDate(projectForm.getFinishDate());
                project.setStartDate(projectForm.getStartDate());
                project.setAttachedFiles(projectForm.getAttachedFiles());
                project.setRequiredProfiles(projectForm.getRequiredProfiles());
                project.setPrivado(projectForm.getPrivado());
                projectService.save(project);

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = updateEditModelAndViewProject(projectForm, "ERROR AL ACTUALIZAR EL PROYECTO");
            }

        return result;
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST, params = "saveProject")
    public ModelAndView save(@Valid ProjectForm projectForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewProject(projectForm);
        else if(projectForm.getTerms() == false){
            result = createEditModelAndViewProject(projectForm,"Acepte los Términos");
        } else
            try {
                Project project = projectService.create();
                project.setComplexity(projectForm.getComplexity());
                project.setDescription(projectForm.getDescription());
                project.setName(projectForm.getName());
                project.setImage(projectForm.getImage());
                project.setMaxParticipants(projectForm.getMaxParticipants());
                project.setFinishDate(projectForm.getFinishDate());
                project.setStartDate(projectForm.getStartDate());
                project.setAttachedFiles(projectForm.getAttachedFiles());
                project.setRequiredProfiles(projectForm.getRequiredProfiles());
                project.setPrivado(projectForm.getPrivado());

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
        result.addObject("message", message);

        return result;
    }

}
