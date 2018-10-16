package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
import src.redtalent.services.UtilidadesService;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/project")
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

        result = new ModelAndView("project/index");
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

    // RegisterUser ---------------------------------------------------------------
    //GET--------------------------------------------------------------
    @RequestMapping(value = "/createProject", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView result;
        ProjectForm projectForm;

        projectForm = new ProjectForm();
        result = createEditModelAndViewProject(projectForm);

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
                project.setComplexity(projectForm.getComplexity());
                project.setDescription(projectForm.getDescription());
                project.setName(projectForm.getName());
                project.setImage(projectForm.getImage());
                project.setMaxParticipants(projectForm.getMaxParticipants());
                project.setFinishDate(projectForm.getFinishDate());
                project.setStartDate(projectForm.getStartDate());
                project.setAttachedFiles(projectForm.getAttachedFiles());
                project.setRequiredProfiles(projectForm.getRequiredProfiles());
                projectService.save(project);
                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = createEditModelAndViewProject(projectForm, "ERROR AL CREAR EL PROYECTO");
            }

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
