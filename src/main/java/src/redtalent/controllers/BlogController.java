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
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/blog")
public class BlogController {

    // Services ---------------------------------------------------------------
    @Autowired
    private BlogService blogService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


    // Constructors -----------------------------------------------------------
    public BlogController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Blog> blogs;

        blogs = blogService.findAll();

        result = new ModelAndView("blog/list");
        result.addObject("requestURI", "blog/list");
        result.addObject("blogs", blogs);

        return result;
    }


    // Crear Blog -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        BlogForm blogForm = new BlogForm();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        try {

            result = new ModelAndView("blog/create");
            result.addObject("blogForm", blogForm);
            result.addObject("requestURI", "./blog/create");
            result.addObject("userCreated", userCreated);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/blog/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid BlogForm blogForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        if (binding.hasErrors()) {
            result = createModelAndView(blogForm);
        } else {
            try {
                Assert.notNull(blogForm, "No puede ser nulo el formulario de BlogForm");

                Blog blog = blogService.create();
                blog.setTitle(blogForm.getTitle());
                blog.setBody(blogForm.getBody());
                blog.setImage(blogForm.getImage());
                Blog saved = this.blogService.save(blog);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());
                Set<Blog> blogs = user.getBlogs();
                blogs.add(saved);
                user.setBlogs(blogs);
                userService.saveUser(user);

                result = new ModelAndView("redirect:/blog/list");

            } catch (Throwable oops) {
                result = createModelAndView(blogForm, "No se puede crear correctamente los temas del blog");

            }
        }
        return result;
    }

    // Crear lista de comentarios para el foro

    @RequestMapping(value = "/listComment", method = RequestMethod.GET)
    public ModelAndView listComments(@RequestParam ObjectId blogId){

        ModelAndView result;
        Collection<Comment> comments;
        Blog blog = blogService.findOne(blogId.toString());

        comments = blog.getComments();

        result = new ModelAndView("blog/listComment");
        result.addObject("requestURI", "blog/listComment?blogId="+blogId);
        result.addObject("comments", comments);
        result.addObject("blogId", blogId);

        return result;
    }

    // Crear comentarios para el foro

    @RequestMapping(value = "/createComment", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId blogId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        CommentForm commentForm = new CommentForm();
        commentForm.setBlogId(blogId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        try {

            result = new ModelAndView("blog/createComment");
            result.addObject("commentForm", commentForm);
            result.addObject("blogId", blogId);
            result.addObject("requestURI", "./blog/createComment?blogId=" +blogId);
            result.addObject("userCreated", userCreated);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/blog/list");
        }
        return result;
    }

    @RequestMapping(value = "/createComment", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setBlogId(commentForm.getBlogId());

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

                Blog blog = blogService.findOne(commentForm.getBlogId().toString());
                List<Comment> comments1 = blog.getComments();
                comments1.add(saved);
                blog.setComments(comments1);
                blogService.save(blog);

                result = new ModelAndView("redirect:/blog/listComment?blogId=" +commentForm.getBlogId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios del blog");

            }
        }
        return result;
    }

    // Crear respuestas para los comentarios del foro


    // Model and View ---------------

    protected ModelAndView createModelAndView(BlogForm blogForm) {
        ModelAndView result;
        result = createModelAndView(blogForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(BlogForm blogForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("blog/create");
        result.addObject("blogForm", blogForm);
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
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

}
