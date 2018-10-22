package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.redtalent.domain.*;
import src.redtalent.forms.AreaForm;
import src.redtalent.forms.GradeForm;
import src.redtalent.forms.TeamForm;
import src.redtalent.services.ProjectService;
import src.redtalent.services.TeamService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UtilidadesService utilidadesService;

    // Constructors -----------------------------------------------------------
    public TeamController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/byProject", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam ObjectId projectId) {

        ModelAndView result;
        Team team;

        Project project = projectService.findOne(projectId.toString());
        team = teamService.teamByProjectId(project);

        result = new ModelAndView("team/display");
        result.addObject("requestURI", "team/byProject?projectId=" +projectId);
        result.addObject("team", team);


        return result;
    }

    @RequestMapping(value = "/teamData", method = RequestMethod.GET)
    public ModelAndView team(@RequestParam ObjectId teamId) {
        ModelAndView result;
        result = new ModelAndView("team/teamData");
        Team team = teamService.findOne(teamId.toString());
        List<User> usuariosTeam = utilidadesService.usuariosDelEquipo(team);

        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usuariosTeam", usuariosTeam);

        return result;
    }

    //Crear equipo -----------------------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        TeamForm teamForm = new TeamForm();
        teamForm.setProjectId(projectId);

        try {
            //Assert.isTrue(teamForm.isClosed(), "No se puede crear un equipo que no está cerrado");
            result = new ModelAndView("team/create");
            result.addObject("teamForm", teamForm);
            result.addObject("projectId", projectId);
            result.addObject("requestURI", "./team/create?projectId="+projectId);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/user/index");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid TeamForm teamForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        teamForm.getProjectId();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = utilidadesService.userConectado(authentication.getName());

        if (binding.hasErrors()) {
            result = createModelAndView(teamForm);
        } else {
            try {
                Assert.notNull(teamForm, "No puede ser nulo el areaForm");

                Team team = teamService.create();
                team.setName(teamForm.getName());
                team.setDescription(teamForm.getDescription());
                Project project = projectService.findOne(teamForm.getProjectId().toString());
                List<Project> projects = new ArrayList<Project>();
                projects.add(project);
                team.setProjects(projects);
                Team saved = this.teamService.save(team);

                Set<Team> teams = new HashSet<Team>();
                teams.addAll(principal.getTeams());
                teams.add(saved);
                principal.setTeams(teams);
                userService.saveUser(principal);

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = createModelAndView(teamForm, "No se puede crear correctamente el equipo");

            }
        }
        return result;
    }

    // Actualizar equipo -----------------------

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam ObjectId teamId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        TeamForm teamForm;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = utilidadesService.userConectado(authentication.getName());

        try {

            Team team = teamService.findOne(teamId.toString());

            teamForm = new TeamForm();
            teamForm.setTeamId(team.getId());
            teamForm.setName(team.getName());
            teamForm.setDescription(team.getDescription());

            Assert.isTrue(principal.equals(teamForm.getUserCreated()), "Usted no tiene acceso a experiencias laborales que no son suyas");

            result = new ModelAndView("team/edit");
            result.addObject("teamForm", teamForm);
            result.addObject("requestURI", "./team/edit.do?teamId=" + teamId);
        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/home/index");
        }

        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid TeamForm teamForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = utilidadesService.userConectado(authentication.getName());

        if (binding.hasErrors()) {
            result = editModelAndView(teamForm);
        } else {
            try {
                Assert.notNull(teamForm, "El equipo no puede ser nulo");

                Team team = teamService.findOne(teamForm.getTeamId());
                team.setName(teamForm.getName());
                team.setDescription(teamForm.getDescription());
                teamService.save(team);

                result = new ModelAndView("redirect:/home/index");
            } catch (Throwable oops) {
                result = editModelAndView(teamForm, "Error");
            }
        }
        return result;
    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(TeamForm teamForm) {
        ModelAndView result;
        result = createModelAndView(teamForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(TeamForm teamForm, String message) {
        ModelAndView result;


        result = new ModelAndView("team/create");
        result.addObject("teamForm", teamForm);
        result.addObject("projectId", teamForm.getProjectId());
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView editModelAndView(TeamForm teamForm) {
        ModelAndView result;
        result = createModelAndView(teamForm, null);
        return result;
    }

    protected ModelAndView editModelAndView(TeamForm teamForm, String message) {
        ModelAndView result;


        result = new ModelAndView("team/edit");
        result.addObject("teamForm", teamForm);
        result.addObject("projectId", teamForm.getProjectId());
        result.addObject("message", message);
        return result;
    }


}
