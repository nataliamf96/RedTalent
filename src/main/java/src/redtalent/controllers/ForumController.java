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
@RequestMapping("/forum")
public class ForumController {

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
    public ForumController() {
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

        for(Team t: teams) {
            if (t.getProjects().contains(project)) {
                List<User> users = utilidadesService.usuariosDelEquipo(t);
                if (users.contains(userCreated)) {
                    result = new ModelAndView("forum/list");
                    result.addObject("requestURI", "forum/list?projectId=" + projectId);
                    result.addObject("forums", forums);
                    result.addObject("projectId", projectId);
                    result.addObject("categories", categories);
                    result.addObject("userCreated", userCreated);
                }
            }
        }

        return result;
    }


    // Crear Foro -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {
        ModelAndView result = new ModelAndView("redirect:/403");
        ForumForm forumForm = new ForumForm();

        forumForm.setProjectId(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        Project project = projectService.findOne(projectId.toString());
        Collection<Team> teams = teamService.findAll();
        Collection<Category> categories = categoryService.findAll();

        for(Team t: teams) {
            if (t.getProjects().contains(project)) {
                List<User> users = utilidadesService.usuariosDelEquipo(t);
                if (users.contains(userCreated)) {
                    result = new ModelAndView("forum/create");
                    result.addObject("forumForm", forumForm);
                    result.addObject("projectId", projectId);
                    result.addObject("requestURI", "./forum/create?projectId=" + projectId);
                    result.addObject("userCreated", userCreated);
                    result.addObject("categories", categories);

                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid ForumForm forumForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        forumForm.setProjectId(forumForm.getProjectId());

        if (binding.hasErrors()) {
            result = createModelAndView(forumForm);
        } else {
            try {
                Assert.notNull(forumForm, "No puede ser nulo el formulario de ForumForm");
                Project project = projectService.findOne(forumForm.getProjectId().toString());
                Team team = teamService.teamByProjectId(project);
                User userProject = userService.findUserByProjectsContains(project);

                Forum forum = forumService.create();
                forum.setTitle(forumForm.getTitle());
                forum.setBody(forumForm.getBody());
                forum.setImage(forumForm.getImage());
                forum.setCategory(forumForm.getCategory());
                Forum saved = this.forumService.save(forum);

                List<Forum> forums1 = project.getForums();
                forums1.add(saved);
                project.setForums(forums1);
                Project projectSaved = projectService.save(project);

                List<Project> projects1 = team.getProjects();
                projects1.add(projectSaved);
                team.setProjects(projects1);
                teamService.save(team);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());

                if(!user.equals(userProject)){
                    Set<Forum> forums = user.getForums();
                    forums.add(saved);
                    user.setForums(forums);
                    userService.saveUser(user);

                    Set<Project> projects = userProject.getProjects();
                    for(Project p : projects){
                        if(p.equals(project)){
                            p.setForums(forums1);
                        }
                    }
                    userProject.setProjects(projects);

                    Set<Team> teams = userProject.getTeams();
                    for(Team t: teams){
                        if(t.equals(team)){
                            t.setProjects(projects1);
                        }
                    }
                    userProject.setTeams(teams);
                    userService.saveUser(userProject);
                }else{

                    Set<Forum> forums = user.getForums();
                    forums.add(saved);
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
                        if(t.equals(team)){
                            t.setProjects(projects1);
                        }
                    }
                    user.setTeams(teams);

                    userService.saveUser(user);
                }

                result = new ModelAndView("redirect:/forum/list?projectId=" + forumForm.getProjectId());

            } catch (Throwable oops) {
                result = createModelAndView(forumForm, "No se puede crear correctamente los temas del foro");

            }
        }
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


        for(Team t: teams) {
            if (t.getProjects().contains(project)) {
                List<User> users = utilidadesService.usuariosDelEquipo(t);
                if (users.contains(userCreated)) {
                    result = new ModelAndView("forum/listComment");
                    result.addObject("requestURI", "forum/listComment?forumId=" + forumId + "&projectId=" + projectId);
                    result.addObject("comments", comments);
                    result.addObject("forumId", forumId);
                    result.addObject("projectId", projectId);
                    result.addObject("userCreated", userCreated);

                }
            }
        }
        return result;
    }

    // Crear comentarios para el foro

    @RequestMapping(value = "/createComment", method = RequestMethod.GET)
    public ModelAndView createComment(@RequestParam ObjectId forumId, @RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {

        ModelAndView result = new ModelAndView("redirect:/403");
        CommentForm commentForm = new CommentForm();
        commentForm.setForumId(forumId);
        commentForm.setProjectId(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        Project project = projectService.findOne(projectId.toString());
        Collection<Team> teams = teamService.findAll();
        for(Team t: teams){
            if(t.getProjects().contains(project)){
                List<User> users = utilidadesService.usuariosDelEquipo(t);
                if(users.contains(userCreated)){
                    result = new ModelAndView("forum/createComment");
                    result.addObject("commentForm", commentForm);
                    result.addObject("forumId", forumId);
                    result.addObject("projectId", projectId);
                    result.addObject("requestURI", "./forum/createComment?forumId=" +forumId+ "&projectId=" + projectId);
                    result.addObject("userCreated", userCreated);

                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/createComment", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setProjectId(commentForm.getProjectId());
        commentForm.setForumId(commentForm.getForumId());

        if (binding.hasErrors()) {
            result = createCommentModelAndView(commentForm);
        } else {
            try {
                Assert.notNull(commentForm, "No puede ser nulo el formulario de Comment");
                Forum forum = forumService.findOne(commentForm.getForumId().toString());
                User userSaved = userService.findUserByForumsContains(forum);

                Comment comment = commentService.create();
                comment.setTitle(commentForm.getTitle());
                comment.setText(commentForm.getText());
                Comment saved = commentService.save(comment);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());
                Set<Comment> comments = user.getComments();
                comments.add(saved);
                user.setComments(comments);
                userService.saveUser(user);

                List<Comment> comments1 = forum.getComments();
                comments1.add(saved);
                forum.setComments(comments1);
                forumService.save(forum);

                Set<Forum> forums1 = userSaved.getForums();
                for(Forum f1: forums1){
                    f1.setComments(comments1);
                }
                userSaved.setForums(forums1);
                userService.saveUser(userSaved);

                Project project = projectService.findOne(commentForm.getProjectId().toString());
                List<Forum> forums = project.getForums();
                for(Forum f: forums){
                    f.setComments(comments1);
                }
                project.setForums(forums);
                projectService.save(project);

                result = new ModelAndView("redirect:/forum/listComment?forumId=" +commentForm.getForumId()+ "&projectId=" +commentForm.getProjectId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios del foro");

            }
        }
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
        for (Team t : teams) {
            if (t.getProjects().contains(project)) {
                List<User> users = utilidadesService.usuariosDelEquipo(t);
                if (users.contains(userCreated)) {
                    result = new ModelAndView("forum/listReply");
                    result.addObject("requestURI", "forum/listReply?forumId=" + forumId + "&projectId=" + projectId + "&commentId=" + commentId);
                    result.addObject("replies", replies);
                    result.addObject("commentId", commentId);
                    result.addObject("forumId", forumId);
                    result.addObject("projectId", projectId);
                    result.addObject("userCreated", userCreated);
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/createReply", method = RequestMethod.GET)
    public ModelAndView createReply(@RequestParam ObjectId forumId, @RequestParam ObjectId projectId, @RequestParam ObjectId commentId, final RedirectAttributes redirectAttrs) {
        ModelAndView result = new ModelAndView("redirect:/403");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        ReplyForm replyForm = new ReplyForm();
        replyForm.setCommentId(commentId);
        replyForm.setForumId(forumId);
        replyForm.setProjectId(projectId);

        Project project = projectService.findOne(projectId.toString());
        Collection<Team> teams = teamService.findAll();
        for(Team t: teams) {
            if (t.getProjects().contains(project)) {
                List<User> users = utilidadesService.usuariosDelEquipo(t);
                if (users.contains(userCreated)) {
                    result = new ModelAndView("forum/createReply");
                    result.addObject("replyForm", replyForm);
                    result.addObject("requestURI", "./forum/createReply?forumId=" + forumId + "&projectId=" + projectId + "&commentId=" + commentId);
                    result.addObject("commentId", commentId);
                    result.addObject("forumId", forumId);
                    result.addObject("projectId", projectId);
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/createReply", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateReply(@Valid ReplyForm replyForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        replyForm.setCommentId(replyForm.getCommentId());
        replyForm.setProjectId(replyForm.getProjectId());
        replyForm.setForumId(replyForm.getForumId());

        if (binding.hasErrors()) {
            result = createReplyModelAndView(replyForm);
        } else {
            try {
                Assert.notNull(replyForm, "No puede ser nulo el formulario de Reply");
                Forum forum = forumService.findOne(replyForm.getForumId().toString());
                User userSaved = userService.findUserByForumsContains(forum);
                Comment comment = commentService.findOne(replyForm.getCommentId().toString());
                User saved2 = userService.findUserByCommentsContains(comment);

                Reply reply = replyService.create();
                reply.setTitle(replyForm.getTitle());
                reply.setText(replyForm.getText());
                Reply saved = replyService.save(reply);

                Set<Reply> replies1 = comment.getReplies();
                replies1.add(saved);
                comment.setReplies(replies1);
                commentService.save(comment);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());
                Set<Reply> replies = user.getReplies();
                replies.add(saved);
                user.setReplies(replies);
                userService.saveUser(user);

                Set<Forum> forums1 = userSaved.getForums();
                for(Forum f1: forums1){
                    for(Comment c1: f1.getComments()){
                        c1.setReplies(replies1);
                    }
                }
                userSaved.setForums(forums1);
                userService.saveUser(userSaved);

                Set<Comment> comments1 = saved2.getComments();
                for(Comment c: comments1){
                    c.setReplies(replies1);
                }
                saved2.setComments(comments1);
                userService.saveUser(saved2);

                List<Comment> comments = forum.getComments();
                for(Comment c: comments){
                    c.setReplies(replies1);
                }
                forum.setComments(comments);
                forumService.save(forum);

                Project project = projectService.findOne(replyForm.getProjectId().toString());
                List<Forum> forums = project.getForums();
                for(Forum f: forums){
                    f.setComments(comments);
                }
                project.setForums(forums);
                projectService.save(project);

                result = new ModelAndView("redirect:/forum/listReply?forumId=" + replyForm.getForumId() + "&projectId=" +replyForm.getProjectId() +"&commentId=" +replyForm.getCommentId());

            } catch (Throwable oops) {
                result = createReplyModelAndView(replyForm, "No se puede crear correctamente las respuestas del comentario");

            }
        }
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

        if(user.equals(userCreated)) {
            for (Comment c : res.getComments()) {
                commentService.remove(c);
                for (Reply r : c.getReplies()) {
                    replyService.remove(r);
                }
            }

            Set<Forum> forums = user.getForums();
            forums.remove(res);
            user.setForums(forums);
            userService.saveUser(user);

            List<Forum> forums1 = project.getForums();
            forums1.remove(res);
            project.setForums(forums1);
            projectService.save(project);

            res.setCategory(null);
            Forum saved = forumService.save(res);

            forumService.remove(saved);
        }

        result = new ModelAndView("redirect:/forum/list?projectId=" + project.getId());
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
        Forum f = forumService.findForumByCommentsContaining(res);
        Project project = projectService.findProjectByForumsContains(f);

        if(user.equals(userCreated)) {
            Set<Comment> comments = user.getComments();
            comments.remove(res);
            user.setComments(comments);
            User saved = userService.saveUser(user);

            List<Comment> comments1 = f.getComments();
            comments1.remove(res);
            f.setComments(comments1);
            forumService.save(f);

            Set<Forum> forums1 = saved.getForums();
            for(Forum f1: forums1){
                f1.setComments(comments1);
            }
            saved.setForums(forums1);
            userService.saveUser(saved);

            List<Forum> forums = project.getForums();
            for(Forum forum: forums){
                forum.setComments(comments1);
            }
            project.setForums(forums);
            projectService.save(project);

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);
        }

        result = new ModelAndView("redirect:/forum/listComment?forumId=" +f.getId() + "&projectId=" + project.getId());
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
        Comment c = commentService.findCommentByRepliesContaining(res);
        Forum f = forumService.findForumByCommentsContaining(c);
        Project p = projectService.findProjectByForumsContains(f);

        if(user.equals(userCreated)) {

            Set<Reply> replies1 = c.getReplies();
            replies1.remove(res);
            c.setReplies(replies1);
            commentService.save(c);

            List<Comment> comments1 = f.getComments();
            for(Comment com1: comments1){
                com1.setReplies(replies1);
            }
            f.setComments(comments1);
            forumService.save(f);

            List<Forum> forums = p.getForums();
            for(Forum forum: forums){
                forum.setComments(comments1);
            }
            p.setForums(forums);
            projectService.save(p);

            Set<Comment> comments = user.getComments();
            for(Comment com : comments){
                com.setReplies(replies1);
            }
            Set<Reply> replies = user.getReplies();
            replies.remove(res);
            user.setReplies(replies);
            user.setComments(comments);
            User saved = userService.saveUser(user);

            Set<Forum> forums1 = saved.getForums();
            for(Forum f1: forums1){
                for(Comment c1: f1.getComments()){
                    c1.setReplies(replies1);
                }
                f1.setComments(comments1);
            }
            saved.setForums(forums1);
            userService.saveUser(saved);

            replyService.remove(res);
        }

        result = new ModelAndView("redirect:/forum/listReply?forumId=" +f.getId() + "&projectId=" + p.getId() + "&commentId=" + c.getId());
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
