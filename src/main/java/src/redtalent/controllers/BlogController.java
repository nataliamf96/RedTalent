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
import src.redtalent.forms.ReplyForm;
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

    @Autowired
    private ReplyService replyService;


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

    // Crear lista de las respuestas de los comentarios

    @RequestMapping(value = "/listReply", method = RequestMethod.GET)
    public ModelAndView listReplies(@RequestParam ObjectId commentId){

        ModelAndView result;
        Collection<Reply> replies;
        Comment comment = commentService.findOne(commentId.toString());

        replies = comment.getReplies();

        result = new ModelAndView("blog/listReply");
        result.addObject("requestURI", "blog/listReply?commentId="+commentId);
        result.addObject("replies", replies);
        result.addObject("commentId", commentId);

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

    @RequestMapping(value = "/createReply", method = RequestMethod.GET)
    public ModelAndView createReply(@RequestParam ObjectId commentId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        ReplyForm replyForm = new ReplyForm();
        replyForm.setCommentId(commentId);

        try {
            result = new ModelAndView("blog/createReply");
            result.addObject("replyForm", replyForm);
            result.addObject("requestURI", "./blog/createReply?commentId=" + commentId);
            result.addObject("commentId", commentId);
        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/blog/list");
        }
        return result;
    }

    @RequestMapping(value = "/createReply", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateReply(@Valid ReplyForm replyForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        replyForm.setCommentId(replyForm.getCommentId());

        if (binding.hasErrors()) {
            result = createReplyModelAndView(replyForm);
        } else {
            try {
                Assert.notNull(replyForm, "No puede ser nulo el formulario de Reply");

                Reply reply = replyService.create();
                reply.setTitle(replyForm.getTitle());
                reply.setText(replyForm.getText());
                Reply saved = replyService.save(reply);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());
                Set<Reply> replies = user.getReplies();
                replies.add(saved);
                user.setReplies(replies);
                userService.saveUser(user);

                Comment comment = commentService.findOne(replyForm.getCommentId().toString());
                List<Reply> replies1 = comment.getReplies();
                replies1.add(saved);
                comment.setReplies(replies1);
                commentService.save(comment);

                Blog blog = blogService.findBlogByCommentsContaining(comment);
                List<Comment> comments = blog.getComments();
                for(Comment c: comments){
                    c.setReplies(replies1);
                }
                blog.setComments(comments);
                blogService.save(blog);

                result = new ModelAndView("redirect:/blog/listReply?commentId=" +replyForm.getCommentId());

            } catch (Throwable oops) {
                result = createReplyModelAndView(replyForm, "No se puede crear correctamente las respuestas del comentario");

            }
        }
        return result;
    }

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
        result.addObject("commentId", replyForm.getCommentId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }

}
