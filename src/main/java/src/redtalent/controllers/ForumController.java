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

    // Constructors -----------------------------------------------------------
    public ForumController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam ObjectId projectId) {

        ModelAndView result;
        Project project = projectService.findOne(projectId.toString());
        List<Forum> forums = project.getForums();

        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("forum/list");
        result.addObject("requestURI", "forum/list?projectId=" + projectId);
        result.addObject("forums", forums);
        result.addObject("projectId", projectId);
        result.addObject("categories", categories);

        return result;
    }


    // Crear Blog -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId projectId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        ForumForm forumForm = new ForumForm();

        forumForm.setProjectId(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        try {

            Collection<Category> categories = categoryService.findAll();

            result = new ModelAndView("forum/create");
            result.addObject("forumForm", forumForm);
            result.addObject("projectId", projectId);
            result.addObject("requestURI", "./forum/create?projectId=" + projectId);
            result.addObject("userCreated", userCreated);
            result.addObject("categories", categories);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/forum/list?projectId=" +projectId);
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

        result = new ModelAndView("blog/createComment");
        result.addObject("commentForm", commentForm);
        result.addObject("blogId", commentForm.getBlogId());
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
