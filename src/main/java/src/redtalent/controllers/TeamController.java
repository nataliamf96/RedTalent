package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
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
import src.redtalent.forms.*;
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

    @RequestMapping(value = "/teams")
    public ModelAndView teams() {
        ModelAndView result;

        result = new ModelAndView("team/teams");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("teams",teamService.findAll());
        return result;
    }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userP = userService.findByEmail(authentication.getName());

        Team team = teamService.findOne(teamId.toString());
        List<User> usuariosTeam = utilidadesService.usuariosDelEquipo(team);

        result.addObject("team",team);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usuariosTeam", usuariosTeam);
        result.addObject("userP", userP);
        result.addObject("users", userService.findAll());

        return result;
    }

    //Crear equipo -----------------------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        TeamForm teamForm = new TeamForm();
        teamForm.setProjectId(projectId);

        try {
            Assert.isTrue(projectService.findOne(projectId.toString())!=null,"Proyecto No Existente");
            Assert.isTrue(projectService.findOne(projectId.toString()).getEstado()!=true,"El proyecto está borrado");
            Assert.isTrue(projectService.findOne(projectId.toString()).getCerrado()!=true,"El proyecto está Cerrado");

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
                Assert.notNull(teamForm, "No puede ser nulo el Formulario");

                Team team = teamService.create();
                team.setName(teamForm.getName());
                team.setDescription(teamForm.getDescription());
                team.setImage(teamForm.getImage());
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

    @RequestMapping(value = "/updateTeam", method = RequestMethod.GET)
    public ModelAndView updateTeam(@RequestParam ObjectId teamId) {
        ModelAndView result;

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User principal = utilidadesService.userConectado(authentication.getName());

            Team team = teamService.findOne(teamId.toString());

            Assert.notNull(teamService.findOne(teamId.toString()),"El Equipo no existe.");
            Assert.isTrue(teamService.findOne(teamId.toString()).isClosed(),"El proyecto ya está cerrado.");
            Assert.isTrue(principal.getTeams().contains(team),"El usuario no ha creado el Equipo.");

            /*El Equipo ha sido creado por el Usuario logueado*/

            UpdateTeamForm updateTeamForm;
            updateTeamForm = new UpdateTeamForm();
            result = createEditModelAndViewTeam(updateTeamForm);
            result.addObject("updateTeamForm",team);
            result.addObject("teamId",team.getId());
        }catch (Throwable oops) {
            result = new ModelAndView("redirect:/user/index");
        }

        return result;
    }

    @RequestMapping(value = "/updateTeam", method = RequestMethod.POST, params = "saveModTeam")
    public ModelAndView updateTeam(@Valid UpdateTeamForm updateTeamForm,@RequestParam String teamId, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewTeam(updateTeamForm);
        else
            try {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());

                Team team = teamService.findOne(teamId);
                team.setName(updateTeamForm.getName());
                team.setDescription(updateTeamForm.getDescription());
                team.setImage(updateTeamForm.getImage());
                Team teamSave = teamService.save(team);

                Set<Team> listaTeams = user.getTeams();
                listaTeams.remove(team);
                listaTeams.add(teamSave);
                user.setTeams(listaTeams);
                userService.saveUser(user);

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = createEditModelAndViewTeam(updateTeamForm, "Error al actualizar el Equipo");
            }

        return result;
    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(TeamForm teamForm) {
        ModelAndView result;
        result = createModelAndView(teamForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewTeam(UpdateTeamForm updateTeamForm) {
        ModelAndView result;
        result = createEditModelAndViewTeam(updateTeamForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewTeam(UpdateTeamForm updateTeamForm, String message) {
        ModelAndView result;
        result = new ModelAndView("team/updateTeam");
        result.addObject("updateTeamForm", updateTeamForm);
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("message", message);
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

}
