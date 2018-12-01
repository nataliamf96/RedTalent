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
import src.redtalent.forms.CommentForm;
import src.redtalent.forms.ForumForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/comment")
public class CommentController {

    // Services ---------------------------------------------------------------
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeamService teamService;


    // Constructors -----------------------------------------------------------
    public CommentController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam ObjectId userId) {

        ModelAndView result;
        User user = userService.findOne(userId.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        Set<Comment> comments = user.getCommentsReceived();

        result = new ModelAndView("comment/list");
        result.addObject("requestURI", "comment/list?userId=" +userId);
        result.addObject("comments", comments);
        result.addObject("userId", userId);
        result.addObject("userCreated",userCreated);

        return result;
    }

    //Lista comment de projects

    @RequestMapping(value="/project/list", method = RequestMethod.GET)
    public ModelAndView listProject(@RequestParam ObjectId projectId) {

        ModelAndView result;
        Project project = projectService.findOne(projectId.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        List<Comment> comments = project.getComments();

        result = new ModelAndView("comment/listp");
        result.addObject("requestURI", "comment/project/list?projectId=" +projectId);
        result.addObject("comments", comments);
        result.addObject("projectId", projectId);
        result.addObject("userCreated",userCreated);

        return result;
    }

    //Lista comment de teams

    @RequestMapping(value="/team/list", method = RequestMethod.GET)
    public ModelAndView listTeam(@RequestParam ObjectId teamId) {

        ModelAndView result;
        Team team = teamService.findOne(teamId.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        List<Comment> comments = team.getComments();

        result = new ModelAndView("comment/listt");
        result.addObject("requestURI", "comment/team/list?teamId=" +teamId);
        result.addObject("comments", comments);
        result.addObject("teamId", teamId);
        result.addObject("userCreated",userCreated);

        return result;
    }

    // Crear comentarios a personas

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId userId, final RedirectAttributes redirectAttrs) {
        ModelAndView result = new ModelAndView("redirect:/403");
        CommentForm commentForm = new CommentForm();
        commentForm.setUserId(userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        User res = userService.findOne(userId.toString());

        if(!userCreated.equals(res)){
            result = new ModelAndView("comment/create");
            result.addObject("commentForm", commentForm);
            result.addObject("userId", userId);
            result.addObject("requestURI", "./comment/create?userId=" +userId);
            result.addObject("userCreated", userCreated);

        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setUserId(commentForm.getUserId());

        if (binding.hasErrors()) {
            result = createCommentModelAndView(commentForm);
        } else {
            try {
                Assert.notNull(commentForm, "No puede ser nulo el formulario de Comment");

                Comment comment = commentService.create();
                comment.setTitle(commentForm.getTitle());
                comment.setText(commentForm.getText());
                Comment saved = commentService.save(comment);

                User user = userService.findOne(commentForm.getUserId().toString());
                Set<Comment> comments = user.getCommentsReceived();
                comments.add(saved);
                user.setCommentsReceived(comments);
                userService.saveUser(user);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());
                Set<Comment> comments1 = userCreated.getComments();
                comments1.add(saved);
                userCreated.setComments(comments1);
                userService.saveUser(userCreated);

                result = new ModelAndView("redirect:/user/dataUser?userId=" + commentForm.getUserId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios");

            }
        }
        return result;
    }

    //Crear comentarios a proyectos

    @RequestMapping(value = "/project/create", method = RequestMethod.GET)
    public ModelAndView createProject(@RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        CommentForm commentForm = new CommentForm();
        commentForm.setProjectId(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        Project res = projectService.findOne(projectId.toString());

        result = new ModelAndView("comment/createp");
        result.addObject("commentForm", commentForm);
        result.addObject("projectId", projectId);
        result.addObject("requestURI", "./comment/project/create?projectId=" +projectId);
        result.addObject("userCreated", userCreated);

        return result;
    }

    @RequestMapping(value = "/project/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateProject(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setProjectId(commentForm.getProjectId());

        if (binding.hasErrors()) {
            result = createCommentPModelAndView(commentForm);
        } else {
            try {
                Assert.notNull(commentForm, "No puede ser nulo el formulario de Comment");
                Project project = projectService.findOne(commentForm.getProjectId().toString());
                User userProject = userService.findUserByProjectsContains(project);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());
                Team team = teamService.teamByProjectId(project);

                Comment comment = commentService.create();
                comment.setTitle(commentForm.getTitle());
                comment.setText(commentForm.getText());
                Comment saved = commentService.save(comment);

                List<Comment> comments = project.getComments();
                comments.add(saved);
                project.setComments(comments);
                projectService.save(project);

                List<Project> projects = team.getProjects();
                for(Project p : projects){
                    if(p.equals(project)){
                        p.setComments(comments);
                    }
                }
                team.setProjects(projects);
                teamService.save(team);

                Set<Comment> comments1 = userCreated.getComments();
                comments1.add(saved);
                userCreated.setComments(comments1);
                userService.saveUser(userCreated);

                Set<Project> projects1 = userProject.getProjects();
                for(Project p1: projects1){
                    if(p1.equals(project))
                        p1.setComments(comments);
                }
                userProject.setProjects(projects1);

                Set<Team> teams = userProject.getTeams();
                for(Team t: teams){
                    for(Project p2: t.getProjects()){
                        if(p2.equals(project)){
                            p2.setComments(comments);
                        }
                    }
                }
                userProject.setTeams(teams);
                if(userProject.equals(userCreated)){
                    userProject.setComments(comments1);
                }
                userService.saveUser(userProject);

                result = new ModelAndView("redirect:/comment/project/list?projectId=" +commentForm.getProjectId());

            } catch (Throwable oops) {
                result = createCommentPModelAndView(commentForm, "No se puede crear correctamente los comentarios");

            }
        }
        return result;
    }

    // Crear comentarios a equipos

    @RequestMapping(value = "/team/create", method = RequestMethod.GET)
    public ModelAndView createTeam(@RequestParam ObjectId teamId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        CommentForm commentForm = new CommentForm();
        commentForm.setTeamId(teamId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        Team res = teamService.findOne(teamId.toString());

        result = new ModelAndView("comment/createt");
        result.addObject("commentForm", commentForm);
        result.addObject("teamId", teamId);
        result.addObject("requestURI", "./comment/team/create?teamId=" +teamId);
        result.addObject("userCreated", userCreated);

        return result;
    }

    @RequestMapping(value = "/team/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateTeam(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setProjectId(commentForm.getTeamId());

        if (binding.hasErrors()) {
            result = createCommentTModelAndView(commentForm);
        } else {
            try {
                Assert.notNull(commentForm, "No puede ser nulo el formulario de Comment");
                Team team = teamService.findOne(commentForm.getTeamId().toString());
                User userTeam = userService.findUserByTeamsConstains(team);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());

                Comment comment = commentService.create();
                comment.setTitle(commentForm.getTitle());
                comment.setText(commentForm.getText());
                Comment saved = commentService.save(comment);

                List<Comment> comments = team.getComments();
                comments.add(saved);
                team.setComments(comments);
                teamService.save(team);

                Set<Comment> comments1 = userCreated.getComments();
                comments1.add(saved);
                userCreated.setComments(comments1);
                userService.saveUser(userCreated);

                Set<Team> teams = userTeam.getTeams();
                for(Team t: teams){
                    if(t.equals(team)){
                        t.setComments(comments);
                    }
                }
                if(userCreated.equals(userTeam)){
                    userTeam.setComments(comments1);
                }
                userTeam.setTeams(teams);
                userService.saveUser(userTeam);

                result = new ModelAndView("redirect:/comment/team/list?teamId=" +commentForm.getTeamId());

            } catch (Throwable oops) {
                result = createCommentTModelAndView(commentForm, "No se puede crear correctamente los comentarios");

            }
        }
        return result;
    }

    //Eliminar un comentario

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView deleteComment(@RequestParam ObjectId commentId) {
        ModelAndView result;
        Assert.notNull(commentService.findOne(commentId.toString()), "La id no puede ser nula");

        Comment res = commentService.findOne(commentId.toString());
        User userCreador = userService.findUserByCommentsContains(res);
        User user = userService.findUserByCommentsReceivedContains(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        if (userCreador.equals(userCreated)) {
            Set<Comment> comments = user.getCommentsReceived();
            comments.remove(res);
            user.setCommentsReceived(comments);
            userService.saveUser(user);

            Set<Comment> comments1 = userCreador.getComments();
            comments1.remove(res);
            userCreador.setComments(comments1);
            userService.saveUser(userCreador);

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);
        }

        result = new ModelAndView("redirect:/user/dataUser?userId=" +user.getId());
        return result;
    }

    // Eliminar un comentario de un projecto

    @RequestMapping(value = "/project/delete", method = RequestMethod.GET)
    public ModelAndView deleteCommentProject(@RequestParam ObjectId commentId) {
        ModelAndView result;
        Assert.notNull(commentService.findOne(commentId.toString()), "La id no puede ser nula");

        Comment res = commentService.findOne(commentId.toString());
        User userCreador = userService.findUserByCommentsContains(res);
        Project project = projectService.findProjectByCommentsContains(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Team team = teamService.teamByProjectId(project);
        User userProject = userService.findUserByProjectsContains(project);

        if (userCreador.equals(userCreated)) {
            List<Comment> comments = project.getComments();
            comments.remove(res);
            project.setComments(comments);
            projectService.save(project);

            List<Project> projects = team.getProjects();
            for (Project p : projects) {
                if (p.equals(project)) {
                    p.setComments(comments);
                }
            }
            team.setProjects(projects);
            teamService.save(team);

            Set<Comment> comments1 = userCreated.getComments();
            comments1.remove(res);
            userCreated.setComments(comments1);
            userService.saveUser(userCreated);

            Set<Project> projects1 = userProject.getProjects();
            for (Project p1 : projects1) {
                if (p1.equals(project))
                    p1.setComments(comments);
            }
            userProject.setProjects(projects1);

            Set<Team> teams = userProject.getTeams();
            for (Team t : teams) {
                for (Project p2 : t.getProjects()) {
                    if (p2.equals(project)) {
                        p2.setComments(comments);
                    }
                }
            }
            userProject.setTeams(teams);
            if (userProject.equals(userCreated)) {
                userProject.setComments(comments1);
            }
            userService.saveUser(userProject);

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);
        }

        result = new ModelAndView("redirect:/comment/project/list?projectId=" +project.getId());
        return result;
    }

    // Eliminar un comentario de un equipo

    @RequestMapping(value = "/team/delete", method = RequestMethod.GET)
    public ModelAndView deleteCommentTeam(@RequestParam ObjectId commentId) {
        ModelAndView result;
        Assert.notNull(commentService.findOne(commentId.toString()), "La id no puede ser nula");

        Comment res = commentService.findOne(commentId.toString());
        User userCreador = userService.findUserByCommentsContains(res);
        Team team = teamService.findTeamByCommentsContains(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        User userTeam = userService.findUserByTeamsConstains(team);

        if (userCreador.equals(userCreated)) {
            List<Comment> comments = team.getComments();
            comments.remove(res);
            team.setComments(comments);
            teamService.save(team);

            Set<Comment> comments1 = userCreated.getComments();
            comments1.add(res);
            userCreated.setComments(comments1);
            userService.saveUser(userCreated);

            Set<Team> teams = userTeam.getTeams();
            for(Team t: teams){
                if(t.equals(team)){
                    t.setComments(comments);
                }
            }
            if(userCreated.equals(userTeam)){
                userTeam.setComments(comments1);
            }
            userTeam.setTeams(teams);
            userService.saveUser(userTeam);

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);
        }

        result = new ModelAndView("redirect:/comment/team/list?teamId=" +team.getId());
        return result;
    }


    //Model and View --------------------------------
    protected ModelAndView createCommentModelAndView(CommentForm commentForm) {
        ModelAndView result;
        result = createCommentModelAndView(commentForm, null);
        return result;
    }

    protected ModelAndView createCommentModelAndView(CommentForm commentForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("comment/create");
        result.addObject("commentForm", commentForm);
        result.addObject("userId", commentForm.getUserId());
        result.addObject("projectId", commentForm.getProjectId());
        result.addObject("teamId", commentForm.getTeamId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView createCommentPModelAndView(CommentForm commentForm) {
        ModelAndView result;
        result = createCommentPModelAndView(commentForm, null);
        return result;
    }

    protected ModelAndView createCommentPModelAndView(CommentForm commentForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("comment/createp");
        result.addObject("commentForm", commentForm);
        result.addObject("userId", commentForm.getUserId());
        result.addObject("projectId", commentForm.getProjectId());
        result.addObject("teamId", commentForm.getTeamId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView createCommentTModelAndView(CommentForm commentForm) {
        ModelAndView result;
        result = createCommentTModelAndView(commentForm, null);
        return result;
    }

    protected ModelAndView createCommentTModelAndView(CommentForm commentForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("comment/createt");
        result.addObject("commentForm", commentForm);
        result.addObject("userId", commentForm.getUserId());
        result.addObject("projectId", commentForm.getProjectId());
        result.addObject("teamId", commentForm.getTeamId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }
}
