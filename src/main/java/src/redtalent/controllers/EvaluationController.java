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
import src.redtalent.forms.CategoryForm;
import src.redtalent.forms.DepartmentForm;
import src.redtalent.forms.EvaluationForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

    // Services ---------------------------------------------------------------
    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeamService teamService;

    // Constructors -----------------------------------------------------------
    public EvaluationController() {
        super();
    }

    // Crear evaluacion a persona ----------------------------

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId userId, final RedirectAttributes redirectAttrs) {
        ModelAndView result = new ModelAndView("redirect:/403");
        EvaluationForm evaluationForm = new EvaluationForm();
        evaluationForm.setUserId(userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        User res = userService.findOne(userId.toString());

        if(!userCreated.equals(res)) {
            result = new ModelAndView("evaluation/userCreate");
            result.addObject("evaluationForm", evaluationForm);
            result.addObject("requestURI", "./evaluation/user/create?userId=" + userId);
            result.addObject("userId", userId);
            result.addObject("userCreated", userCreated);
        }

        return result;
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid EvaluationForm evaluationForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        evaluationForm.setUserId(evaluationForm.getUserId());

        if (binding.hasErrors()) {
            result = createModelAndView(evaluationForm);
        } else {
            try {
                Assert.notNull(evaluationForm, "No puede ser nulo el formulario");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());

                Evaluation evaluation = evaluationService.create();
                evaluation.setRate(evaluationForm.getRate());
                Evaluation saved = this.evaluationService.save(evaluation);

                User user = userService.findOne(evaluationForm.getUserId().toString());
                List<Evaluation> evaluations = user.getEvaluationsReceived();
                evaluations.add(saved);
                user.setEvaluationsReceived(evaluations);
                userService.saveUser(user);

                List<Evaluation> evaluations1 = userCreated.getEvaluations();
                evaluations1.add(saved);
                userCreated.setEvaluations(evaluations1);
                userService.saveUser(userCreated);

                result = new ModelAndView("redirect:/comment/list?userId=" + evaluationForm.getUserId());

            } catch (Throwable oops) {
                result = createModelAndView(evaluationForm, "No se puede crear correctamente la evaluacion");

            }
        }
        return result;
    }

    // Crear evaluacion a proyecto ----------------------------

    @RequestMapping(value = "/project/create", method = RequestMethod.GET)
    public ModelAndView createProject(@RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        EvaluationForm evaluationForm = new EvaluationForm();
        evaluationForm.setProjectId(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("evaluation/projectCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("requestURI", "./evaluation/project/create?userId=" + projectId);
        result.addObject("projectId", projectId);
        result.addObject("userCreated", userCreated);


        return result;
    }

    @RequestMapping(value = "/project/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateProject(@Valid EvaluationForm evaluationForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        evaluationForm.setProjectId(evaluationForm.getProjectId());

        if (binding.hasErrors()) {
            result = createProjectModelAndView(evaluationForm);
        } else {
            try {
                Assert.notNull(evaluationForm, "No puede ser nulo el formulario");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());
                Project project = projectService.findOne(evaluationForm.getProjectId().toString());
                Team team = teamService.teamByProjectId(project);
                User userProject = userService.findUserByProjectsContains(project);

                Evaluation evaluation = evaluationService.create();
                evaluation.setRate(evaluationForm.getRate());
                Evaluation saved = this.evaluationService.save(evaluation);

                List<Evaluation> evaluations = project.getEvaluations();
                evaluations.add(saved);
                project.setEvaluations(evaluations);
                projectService.save(project);

                List<Project> projects = team.getProjects();
                for(Project p : projects){
                    if(p.equals(project)){
                        p.setEvaluations(evaluations);
                    }
                }
                team.setProjects(projects);
                teamService.save(team);

                List<Evaluation> evaluations1 = userCreated.getEvaluations();
                evaluations1.add(saved);
                userCreated.setEvaluations(evaluations1);
                userService.saveUser(userCreated);

                Set<Project> projects1 = userProject.getProjects();
                for(Project p1: projects1){
                    if(p1.equals(project))
                    p1.setEvaluations(evaluations);
                }
                userProject.setProjects(projects1);

                Set<Team> teams = userProject.getTeams();
                for(Team t: teams){
                    for(Project p2: t.getProjects()){
                        if(p2.equals(project)){
                            p2.setEvaluations(evaluations);
                        }
                    }
                }
                userProject.setTeams(teams);
                if(userProject.equals(userCreated)){
                    userProject.setEvaluations(evaluations1);
                }
                userService.saveUser(userProject);

                result = new ModelAndView("redirect:/comment/project/list?projectId=" + evaluationForm.getProjectId());

            } catch (Throwable oops) {
                result = createProjectModelAndView(evaluationForm, "No se puede crear correctamente la evaluacion");

            }
        }
        return result;
    }


    // Crear evaluacion a equipo --------------------------------

    @RequestMapping(value = "/team/create", method = RequestMethod.GET)
    public ModelAndView createTeam(@RequestParam ObjectId teamId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        EvaluationForm evaluationForm = new EvaluationForm();
        evaluationForm.setTeamId(teamId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("evaluation/teamCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("requestURI", "./evaluation/team/create?userId=" + teamId);
        result.addObject("teamId", teamId);
        result.addObject("userCreated", userCreated);


        return result;
    }

    @RequestMapping(value = "/team/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateTeam(@Valid EvaluationForm evaluationForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        evaluationForm.setTeamId(evaluationForm.getTeamId());

        if (binding.hasErrors()) {
            result = createTeamModelAndView(evaluationForm);
        } else {
            try {
                Assert.notNull(evaluationForm, "No puede ser nulo el formulario");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());
                Team team = teamService.findOne(evaluationForm.getTeamId().toString());
                User userTeam = userService.findUserByTeamsConstains(team);

                Evaluation evaluation = evaluationService.create();
                evaluation.setRate(evaluationForm.getRate());
                Evaluation saved = this.evaluationService.save(evaluation);

                List<Evaluation> evaluations = team.getEvaluations();
                evaluations.add(saved);
                team.setEvaluations(evaluations);
                teamService.save(team);

                List<Evaluation> evaluations1 = userCreated.getEvaluations();
                evaluations1.add(saved);
                userCreated.setEvaluations(evaluations1);
                userService.saveUser(userCreated);

                Set<Team> teams = userTeam.getTeams();
                for(Team t: teams){
                    if(t.equals(team)){
                        t.setEvaluations(evaluations);
                    }
                }
                userTeam.setTeams(teams);
                if(userTeam.equals(userCreated)){
                    userTeam.setEvaluations(evaluations1);
                }
                userService.saveUser(userTeam);

                result = new ModelAndView("redirect:/comment/team/list?teamId=" + evaluationForm.getTeamId());

            } catch (Throwable oops) {
                result = createTeamModelAndView(evaluationForm, "No se puede crear correctamente la evaluacion");

            }
        }
        return result;
    }


    // Model and View ---------------

    protected ModelAndView createModelAndView(EvaluationForm evaluationForm) {
        ModelAndView result;
        result = createModelAndView(evaluationForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(EvaluationForm evaluationForm, String message) {
        ModelAndView result;

        result = new ModelAndView("evaluation/userCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("userId", evaluationForm.getUserId());
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView createProjectModelAndView(EvaluationForm evaluationForm) {
        ModelAndView result;
        result = createProjectModelAndView(evaluationForm, null);
        return result;
    }

    protected ModelAndView createProjectModelAndView(EvaluationForm evaluationForm, String message) {
        ModelAndView result;

        result = new ModelAndView("evaluation/projectCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("projectId", evaluationForm.getProjectId());
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView createTeamModelAndView(EvaluationForm evaluationForm) {
        ModelAndView result;
        result = createTeamModelAndView(evaluationForm, null);
        return result;
    }

    protected ModelAndView createTeamModelAndView(EvaluationForm evaluationForm, String message) {
        ModelAndView result;

        result = new ModelAndView("evaluation/teamCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("teamId", evaluationForm.getTeamId());
        result.addObject("message", message);
        return result;
    }

}
