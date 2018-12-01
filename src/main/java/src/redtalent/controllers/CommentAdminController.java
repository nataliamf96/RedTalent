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
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {

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
    public CommentAdminController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Collection<User> users = userService.findAll();
        Set<Comment> comments = new HashSet<>();

        for(User u: users){
           comments.addAll(u.getCommentsReceived());
        }

        result = new ModelAndView("admin/comment/list");
        result.addObject("requestURI", "admin/comment/list");
        result.addObject("comments", comments);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    //Lista comment de projects

    @RequestMapping(value="/project/list", method = RequestMethod.GET)
    public ModelAndView listProject() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        List<Comment> comments = new ArrayList<>();

        for(Project project: projectService.findAll()) {
             comments.addAll(project.getComments());
        }

        result = new ModelAndView("admin/comment/listp");
        result.addObject("requestURI", "admin/comment/project/list");
        result.addObject("comments", comments);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    //Lista comment de teams

    @RequestMapping(value="/team/list", method = RequestMethod.GET)
    public ModelAndView listTeam() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        List<Comment> comments = new ArrayList<>();

        for(Team team: teamService.findAll()) {
            comments.addAll(team.getComments());
        }

        result = new ModelAndView("admin/comment/listt");
        result.addObject("requestURI", "admin/comment/team/list");
        result.addObject("comments", comments);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

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

        result = new ModelAndView("redirect:/admin/comment/list");
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
        Team team = teamService.teamByProjectId(project);
        User userProject = userService.findUserByProjectsContains(project);

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

            Set<Comment> comments1 = userCreador.getComments();
            comments1.remove(res);
            userCreador.setComments(comments1);
            userService.saveUser(userCreador);

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
            if (userProject.equals(userCreador)) {
                userProject.setComments(comments1);
            }
            userService.saveUser(userProject);

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);

        result = new ModelAndView("redirect:/admin/comment/project/list");
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
        User userTeam = userService.findUserByTeamsConstains(team);

            List<Comment> comments = team.getComments();
            comments.remove(res);
            team.setComments(comments);
            teamService.save(team);

            Set<Comment> comments1 = userCreador.getComments();
            comments1.add(res);
            userCreador.setComments(comments1);
            userService.saveUser(userCreador);

            Set<Team> teams = userTeam.getTeams();
            for(Team t: teams){
                if(t.equals(team)){
                    t.setComments(comments);
                }
            }
            if(userCreador.equals(userTeam)){
                userTeam.setComments(comments1);
            }
            userTeam.setTeams(teams);
            userService.saveUser(userTeam);

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);

        result = new ModelAndView("redirect:/admin/comment/team/list");
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
        result = createCommentModelAndView(commentForm, null);
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
        result = createCommentModelAndView(commentForm, null);
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
