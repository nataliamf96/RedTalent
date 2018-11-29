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
import src.redtalent.forms.ReplyForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/forum")
public class ForumAdminController {

    // Services ---------------------------------------------------------------

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private TeamService teamService;

    // Constructors -----------------------------------------------------------
    public ForumAdminController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam ObjectId projectId) {

        ModelAndView result = new ModelAndView("redirect:/403");
        Project project = projectService.findOne(projectId.toString());
        List<Forum> forums = project.getForums();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Collection<Team> teams = teamService.findAll();
        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("admin/forum/list");
        result.addObject("requestURI", "admin/forum/list?projectId=" + projectId);
        result.addObject("forums", forums);
        result.addObject("projectId", projectId);
        result.addObject("categories", categories);
        result.addObject("userCreated", userCreated);

        return result;
    }

    // Crear lista de comentarios para el foro

    @RequestMapping(value = "/listComment", method = RequestMethod.GET)
    public ModelAndView listComments(@RequestParam ObjectId forumId, @RequestParam Object projectId){

        ModelAndView result = new ModelAndView("redirect:/403");
        Collection<Comment> comments;
        Forum forum = forumService.findOne(forumId.toString());

        comments = forum.getComments();
        Collection<Team> teams = teamService.findAll();

        Project project = projectService.findOne(projectId.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("admin/forum/listComment");
        result.addObject("requestURI", "admin/forum/listComment?forumId=" + forumId + "&projectId=" + projectId);
        result.addObject("comments", comments);
        result.addObject("forumId", forumId);
        result.addObject("projectId", projectId);
        result.addObject("userCreated", userCreated);

        return result;
    }

    // Lista de respuestas

    @RequestMapping(value = "/listReply", method = RequestMethod.GET)
    public ModelAndView listReplies(@RequestParam ObjectId forumId, @RequestParam ObjectId projectId, @RequestParam ObjectId commentId) {

        ModelAndView result = new ModelAndView("redirect:/403");
        Collection<Reply> replies;
        Comment comment = commentService.findOne(commentId.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        replies = comment.getReplies();
        Project project = projectService.findOne(projectId.toString());
        Collection<Team> teams = teamService.findAll();

        result = new ModelAndView("admin/forum/listReply");
        result.addObject("requestURI", "admin/forum/listReply?forumId=" + forumId + "&projectId=" + projectId + "&commentId=" + commentId);
        result.addObject("replies", replies);
        result.addObject("commentId", commentId);
        result.addObject("forumId", forumId);
        result.addObject("projectId", projectId);
        result.addObject("userCreated", userCreated);

        return result;
    }

    //Eliminar un tema

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam ObjectId forumId) {
        ModelAndView result;

        Assert.notNull(forumService.findOne(forumId.toString()), "La id no puede ser nula");
        Forum res = forumService.findOne(forumId.toString());
        Project project = projectService.findProjectByForumsContains(res);

        User user = userService.findUserByForumsContains(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        User userProject = userService.findUserByProjectsContains(project);
        Team team = teamService.teamByProjectId(project);

            for (Comment c : res.getComments()) {
                commentService.remove(c);
                for (Reply r : c.getReplies()) {
                    replyService.remove(r);
                }
            }

            List<Forum> forums1 = project.getForums();
            forums1.remove(res);
            project.setForums(forums1);
            projectService.save(project);

            List<Project> projects1 = team.getProjects();
            for(Project p1: projects1){
                if(p1.equals(project)){
                    p1.setForums(forums1);
                }
            }
            team.setProjects(projects1);
            teamService.save(team);

            if(!user.equals(userProject)){
                Set<Forum> forums = user.getForums();
                forums.remove(res);
                user.setForums(forums);
                userService.saveUser(user);

                Set<Project> projects = userProject.getProjects();
                for(Project p : projects){
                    if(p.equals(project)){
                        p.setForums(forums1);
                    }
                }
                userProject.setProjects(projects);

                Set<Team> teams2 = userProject.getTeams();
                for(Team t: teams2){
                    for(Project p: t.getProjects()){
                        if(p.equals(project)){
                            p.setForums(forums1);
                        }
                    }
                }
                userProject.setTeams(teams2);
                userService.saveUser(userProject);

            }else{

                Set<Forum> forums = user.getForums();
                forums.remove(res);
                user.setForums(forums);

                Set<Project> projects = user.getProjects();
                for(Project p : projects){
                    if(p.equals(project)){
                        p.setForums(forums1);
                    }
                }
                user.setProjects(projects);

                Set<Team> teams = user.getTeams();
                for(Team t: teams){
                    for(Project p: t.getProjects()){
                        if(p.equals(project)){
                            p.setForums(forums1);
                        }
                    }
                }
                user.setTeams(teams);

                userService.saveUser(user);

            }

            res.setCategory(null);
            Forum saved = forumService.save(res);

            forumService.remove(saved);

        result = new ModelAndView("redirect:/admin/forum/list?projectId=" + project.getId());
        return result;
    }

    //Eliminar un comentario

    @RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
    public ModelAndView deleteComment(@RequestParam ObjectId commentId) {
        ModelAndView result;
        Assert.notNull(commentService.findOne(commentId.toString()), "La id no puede ser nula");

        Comment res = commentService.findOne(commentId.toString());
        User user = userService.findUserByCommentsContains(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Forum forum = forumService.findForumByCommentsContaining(res);
        Project project = projectService.findProjectByForumsContains(forum);
        User userProject = userService.findUserByProjectsContains(project);
        Team team = teamService.teamByProjectId(project);
        User userForum = userService.findUserByForumsContains(forum);

            List<Comment> comments1 = forum.getComments();
            comments1.remove(res);
            forum.setComments(comments1);
            forumService.save(forum);

            List<Forum> forums = project.getForums();
            for(Forum f: forums){
                if(f.equals(forum)){
                    f.setComments(comments1);
                }
            }
            project.setForums(forums);
            projectService.save(project);

            List<Project> projects1 = team.getProjects();
            for(Project p1 : projects1){
                for(Forum f: p1.getForums()){
                    if(f.equals(forum)){
                        f.setComments(comments1);
                    }
                }
            }
            team.setProjects(projects1);
            teamService.save(team);

            if(user.equals(userForum) && user.equals(userProject)){

                Set<Comment> comments = user.getComments();
                comments.remove(res);
                user.setComments(comments);

                Set<Forum> forums1 = user.getForums();
                for(Forum f1: forums1){
                    if(f1.equals(forum)){
                        f1.setComments(comments1);
                    }
                }
                user.setForums(forums1);

                Set<Project> projects = user.getProjects();
                for(Project pr : projects){
                    for(Forum forum2: pr.getForums()){
                        if(forum2.equals(forum)){
                            forum2.setComments(comments1);
                        }
                    }
                }
                user.setProjects(projects);

                Set<Team> teams = user.getTeams();
                for(Team t: teams){
                    for(Project p: t.getProjects()){
                        for(Forum forum1 : p.getForums()){
                            if(forum1.equals(forum)){
                                forum1.setComments(comments1);
                            }
                        }
                    }
                }
                user.setTeams(teams);
                userService.saveUser(user);

            }else{

                Set<Comment> comments = user.getComments();
                comments.remove(res);
                user.setComments(comments);
                userService.saveUser(user);

                Set<Forum> forums1 = userForum.getForums();
                for(Forum f1: forums1){
                    if(f1.equals(forum)) {
                        f1.setComments(comments1);
                    }
                }
                userForum.setForums(forums1);
                if(user.equals(userForum)){
                    userForum.setComments(comments);
                }
                userService.saveUser(userForum);

                Set<Project> projects = userProject.getProjects();
                for(Project pr : projects){
                    for(Forum forum2: pr.getForums()){
                        if(forum2.equals(forum)){
                            forum2.setComments(comments1);
                        }
                    }
                }
                userProject.setProjects(projects);

                Set<Team> teams = userProject.getTeams();
                for(Team t: teams){
                    for(Project p: t.getProjects()){
                        for(Forum forum1 : p.getForums()){
                            if(forum1.equals(forum)){
                                forum1.setComments(comments1);
                            }
                        }
                    }
                }
                userProject.setTeams(teams);
                if(user.equals(userProject)){
                    userProject.setComments(comments);
                }
                if(userProject.equals(userForum)){
                    userProject.setForums(forums1);
                }
                userService.saveUser(userProject);
            }

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);

        result = new ModelAndView("redirect:/admin/forum/listComment?forumId=" +forum.getId() + "&projectId=" + project.getId());
        return result;
    }

    //Eliminar reply

    @RequestMapping(value = "/deleteReply", method = RequestMethod.GET)
    public ModelAndView deleteReply(@RequestParam ObjectId replyId) {
        ModelAndView result;
        Assert.notNull(replyService.findOne(replyId.toString()), "La id no puede ser nula");

        Reply res = replyService.findOne(replyId.toString());
        User user = userService.findUserByRepliesContaining(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Comment comment = commentService.findCommentByRepliesContaining(res);
        Forum forum = forumService.findForumByCommentsContaining(comment);
        Project project = projectService.findProjectByForumsContains(forum);
        Team team = teamService.teamByProjectId(project);
        User userComment = userService.findUserByCommentsContains(comment);
        User userProject = userService.findUserByProjectsContains(project);
        User userForum = userService.findUserByForumsContains(forum);

            Set<Reply> replies1 = comment.getReplies();
            replies1.remove(res);
            comment.setReplies(replies1);
            commentService.save(comment);

            List<Comment> comments = forum.getComments();
            for(Comment c: comments){
                if(c.equals(comment)) {
                    c.setReplies(replies1);
                }
            }
            forum.setComments(comments);
            forumService.save(forum);

            List<Forum> forums = project.getForums();
            for(Forum f: forums){
                for(Comment c : f.getComments()){
                    if(c.equals(comment)){
                        c.setReplies(replies1);
                    }
                }
            }
            project.setForums(forums);
            projectService.save(project);

            List<Project> projects1 = team.getProjects();
            for(Project p1 : projects1){
                for(Forum f: p1.getForums()){
                    for(Comment c: f.getComments()){
                        if(c.equals(comment)){
                            c.setReplies(replies1);
                        }
                    }
                }
            }
            team.setProjects(projects1);
            teamService.save(team);

            if(user.equals(userProject) && user.equals(userForum) && user.equals(userComment)){

                Set<Reply> replies = user.getReplies();
                replies.remove(res);
                user.setReplies(replies);

                Set<Comment> comments1 = user.getComments();
                for(Comment c: comments1){
                    if(c.equals(comment)) {
                        c.setReplies(replies1);
                    }
                }
                user.setComments(comments1);

                Set<Forum> forums1 = user.getForums();
                for(Forum f1: forums1){
                    for(Comment c1: f1.getComments()){
                        if(c1.equals(comment)) {
                            c1.setReplies(replies1);
                        }
                    }
                }
                user.setForums(forums1);

                Set<Project> projects = user.getProjects();
                for(Project pr : projects){
                    for(Forum forum2: pr.getForums()){
                        for(Comment comment2: forum2.getComments()){
                            if(comment2.equals(comment)){
                                comment2.setReplies(replies1);
                            }
                        }
                    }
                }
                user.setProjects(projects);

                Set<Team> teams = user.getTeams();
                for(Team t: teams){
                    for(Project p: t.getProjects()){
                        for(Forum forum1 : p.getForums()){
                            for(Comment comment1: forum1.getComments()){
                                if(comment1.equals(comment)){
                                    comment1.setReplies(replies1);
                                }
                            }
                        }
                    }
                }
                user.setTeams(teams);

                userService.saveUser(user);

            } else{

                Set<Reply> replies = user.getReplies();
                replies.remove(res);
                user.setReplies(replies);
                userService.saveUser(user);

                Set<Comment> comments1 = userComment.getComments();
                for(Comment c: comments1){
                    if(c.equals(comment)) {
                        c.setReplies(replies1);
                    }
                }
                if(userComment.equals(user)){
                    userComment.setReplies(replies);
                }
                userComment.setComments(comments1);
                userService.saveUser(userComment);

                Set<Forum> forums1 = userForum.getForums();
                for(Forum f1: forums1){
                    for(Comment c1: f1.getComments()){
                        if(c1.equals(comment)) {
                            c1.setReplies(replies1);
                        }
                    }
                }
                if(userForum.equals(userComment)){
                    userForum.setComments(comments1);
                }
                if(userForum.equals(user)){
                    userForum.setReplies(replies);
                }
                userForum.setForums(forums1);
                userService.saveUser(userForum);

                Set<Project> projects = userProject.getProjects();
                for(Project pr : projects){
                    for(Forum forum2: pr.getForums()){
                        for(Comment comment2: forum2.getComments()){
                            if(comment2.equals(comment)){
                                comment2.setReplies(replies1);
                            }
                        }
                    }
                }
                userProject.setProjects(projects);

                Set<Team> teams = userProject.getTeams();
                for(Team t: teams){
                    for(Project p: t.getProjects()){
                        for(Forum forum1 : p.getForums()){
                            for(Comment comment1: forum1.getComments()){
                                if(comment1.equals(comment)){
                                    comment1.setReplies(replies1);
                                }
                            }
                        }
                    }
                }
                if(userProject.equals(user)){
                    userProject.setReplies(replies);
                }
                if(userProject.equals(userComment)){
                    userProject.setComments(comments1);
                }
                if(userProject.equals(userForum)){
                    userProject.setForums(forums1);
                }
                userProject.setTeams(teams);
                userService.saveUser(userProject);
            }

            replyService.remove(res);

        result = new ModelAndView("redirect:/admin/forum/listReply?forumId=" +forum.getId() + "&projectId=" + project.getId() + "&commentId=" + comment.getId());
        return result;
    }


    // Model and View ---------------

    protected ModelAndView createModelAndView(ForumForm forumForm) {
        ModelAndView result;
        result = createModelAndView(forumForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(ForumForm forumForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("forum/create");
        result.addObject("forumForm", forumForm);
        result.addObject("userCreated", userCreated);
        result.addObject("projectId", forumForm.getProjectId());
        result.addObject("message", message);
        result.addObject("categories", categories);
        return result;
    }

    protected ModelAndView createCommentModelAndView(CommentForm commentForm) {
        ModelAndView result;
        result = createCommentModelAndView(commentForm, null);
        return result;
    }

    protected ModelAndView createCommentModelAndView(CommentForm commentForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("forum/createComment");
        result.addObject("commentForm", commentForm);
        result.addObject("forumId", commentForm.getForumId());
        result.addObject("projectId", commentForm.getProjectId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView createReplyModelAndView(ReplyForm replyForm) {
        ModelAndView result;
        result = createReplyModelAndView(replyForm, null);
        return result;
    }

    protected ModelAndView createReplyModelAndView(ReplyForm replyForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("forum/createReply");
        result.addObject("replyForm", replyForm);
        result.addObject("forumId", replyForm.getForumId());
        result.addObject("commentId", replyForm.getCommentId());
        result.addObject("projectId", replyForm.getProjectId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }
}
