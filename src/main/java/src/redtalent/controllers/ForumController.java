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
import src.redtalent.forms.BlogForm;
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

                Forum forum = forumService.create();
                forum.setTitle(forumForm.getTitle());
                forum.setBody(forumForm.getBody());
                forum.setImage(forumForm.getImage());
                forum.setCategory(forumForm.getCategory());
                Forum saved = this.forumService.save(forum);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());
                Set<Forum> forums = user.getForums();
                forums.add(saved);
                user.setForums(forums);
                userService.saveUser(user);

                Project project = projectService.findOne(forumForm.getProjectId().toString());
                List<Forum> forums1 = project.getForums();
                forums1.add(saved);
                project.setForums(forums1);
                projectService.save(project);

                result = new ModelAndView("redirect:/forum/list?projectId=" + forumForm.getProjectId());

            } catch (Throwable oops) {
                result = createModelAndView(forumForm, "No se puede crear correctamente los temas del blog");

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
        commentForm.setProjectId(commentForm.getBlogId());
        commentForm.setForumId(commentForm.getForumId());

        if (binding.hasErrors()) {
            result = createCommentModelAndView(commentForm);
        } else {
            try {
                Assert.notNull(commentForm, "No puede ser nulo el formulario de Comment");

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

                Forum forum = forumService.findOne(commentForm.getBlogId().toString());
                List<Comment> comments1 = forum.getComments();
                comments1.add(saved);
                forum.setComments(comments1);
                forumService.save(forum);

                Project project = projectService.findOne(commentForm.getProjectId().toString());
                List<Forum> forums = project.getForums();
                for(Forum f: forums){
                    f.setComments(comments1);
                }
                project.setForums(forums);
                projectService.save(project);

                result = new ModelAndView("redirect:/blog/listComment?forumId=" +commentForm.getBlogId()+ "&projectId=" +commentForm.getProjectId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios del blog");

            }
        }
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

        result = new ModelAndView("blog/createReply");
        result.addObject("replyForm", replyForm);
        result.addObject("blogId", replyForm.getBlogId());
        result.addObject("commentId", replyForm.getCommentId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }

}
