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
import java.util.HashSet;
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

    @Autowired
    private CategoryService categoryService;


    // Constructors -----------------------------------------------------------
    public BlogController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Blog> blogs;
        Collection<Category> categories = categoryService.findAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        blogs = blogService.findAll();

        result = new ModelAndView("blog/list");
        result.addObject("requestURI", "blog/list");
        result.addObject("blogs", blogs);
        result.addObject("categories", categories);
        result.addObject("userCreated", userCreated);

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

            Collection<Category> categories = categoryService.findAll();

            result = new ModelAndView("blog/create");
            result.addObject("blogForm", blogForm);
            result.addObject("requestURI", "./blog/create");
            result.addObject("userCreated", userCreated);
            result.addObject("categories", categories);

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
                blog.setCategory(blogForm.getCategory());
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        comments = blog.getComments();

        result = new ModelAndView("blog/listComment");
        result.addObject("requestURI", "blog/listComment?blogId="+blogId);
        result.addObject("comments", comments);
        result.addObject("blogId", blogId);
        result.addObject("userCreated", userCreated);

        return result;
    }

    // Crear lista de las respuestas de los comentarios

    @RequestMapping(value = "/listReply", method = RequestMethod.GET)
    public ModelAndView listReplies(@RequestParam ObjectId blogId, @RequestParam ObjectId commentId){

        ModelAndView result;
        Collection<Reply> replies;
        Comment comment = commentService.findOne(commentId.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        replies = comment.getReplies();

        result = new ModelAndView("blog/listReply");
        result.addObject("requestURI", "blog/listReply?blogId=" + blogId + "&commentId=" +commentId);
        result.addObject("replies", replies);
        result.addObject("commentId", commentId);
        result.addObject("blogId", blogId);
        result.addObject("userCreated", userCreated);

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
                User userSaved = userService.saveUser(user);

                Blog blog = blogService.findOne(commentForm.getBlogId().toString());
                List<Comment> comments1 = blog.getComments();
                comments1.add(saved);
                blog.setComments(comments1);
                blogService.save(blog);

                Set<Blog> blogs = userSaved.getBlogs();
                for(Blog b: blogs){
                    b.setComments(comments1);
                }
                userSaved.setBlogs(blogs);
                userService.saveUser(userSaved);

                result = new ModelAndView("redirect:/blog/listComment?blogId=" +commentForm.getBlogId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios del blog");

            }
        }
        return result;
    }

    // Crear respuestas para los comentarios del foro

    @RequestMapping(value = "/createReply", method = RequestMethod.GET)
    public ModelAndView createReply(@RequestParam ObjectId blogId, @RequestParam ObjectId commentId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        ReplyForm replyForm = new ReplyForm();
        replyForm.setCommentId(commentId);
        replyForm.setBlogId(blogId);

        try {
            result = new ModelAndView("blog/createReply");
            result.addObject("replyForm", replyForm);
            result.addObject("requestURI", "./blog/createReply?blogId=" + blogId + "&commentId=" + commentId);
            result.addObject("commentId", commentId);
            result.addObject("blogId", blogId);
        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/blog/list");
        }
        return result;
    }

    @RequestMapping(value = "/createReply", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreateReply(@Valid ReplyForm replyForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        replyForm.setCommentId(replyForm.getCommentId());
        replyForm.setBlogId(replyForm.getBlogId());

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
                User userSaved = userService.saveUser(user);

                Comment comment = commentService.findOne(replyForm.getCommentId().toString());
                Set<Reply> replies1 = comment.getReplies();
                replies1.add(saved);
                comment.setReplies(replies1);
                commentService.save(comment);

                Blog blog = blogService.findOne(replyForm.getBlogId().toString());
                List<Comment> comments = blog.getComments();
                for(Comment c: comments){
                    c.setReplies(replies1);
                }
                blog.setComments(comments);
                blogService.save(blog);

                Set<Blog> blogs = userSaved.getBlogs();
                for(Blog b: blogs){
                    for(Comment c1: b.getComments()){
                        c1.setReplies(replies1);
                    }
                }
                userSaved.setBlogs(blogs);
                User saved2 = userService.saveUser(userSaved);

                Set<Comment> comments1 = saved2.getComments();
                for(Comment c: comments1){
                    c.setReplies(replies1);
                }
                saved2.setComments(comments1);
                userService.saveUser(saved2);

                result = new ModelAndView("redirect:/blog/listReply?blogId=" + replyForm.getBlogId() + "&commentId=" +replyForm.getCommentId());

            } catch (Throwable oops) {
                result = createReplyModelAndView(replyForm, "No se puede crear correctamente las respuestas del comentario");

            }
        }
        return result;
    }

    //Eliminar un tema

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam ObjectId blogId) {
        ModelAndView result;

        Assert.notNull(blogService.findOne(blogId.toString()), "La id no puede ser nula");
        Blog res = blogService.findOne(blogId.toString());

        User user = userService.findUserByBlogsContains(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        if(user.equals(userCreated)) {
            for (Comment c : res.getComments()) {
                commentService.remove(c);
                for (Reply r : c.getReplies()) {
                    replyService.remove(r);
                }
            }
            Set<Blog> blogs = user.getBlogs();
            blogs.remove(res);
            user.setBlogs(blogs);
            userService.saveUser(user);

            res.setCategory(null);
            Blog saved = blogService.save(res);

            blogService.remove(saved);
        }

        result = new ModelAndView("redirect:/blog/list");
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
        Blog b = blogService.findBlogByCommentsContaining(res);

        if(user.equals(userCreated)) {
            Set<Comment> comments = user.getComments();
            comments.remove(res);
            user.setComments(comments);
            userService.saveUser(user);

            List<Comment> comments1 = b.getComments();
            comments1.remove(res);
            b.setComments(comments1);
            blogService.save(b);


            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);
        }

        result = new ModelAndView("redirect:/blog/listComment?blogId=" +b.getId());
        return result;
    }

    //Eliminar una respuesta

    @RequestMapping(value = "/deleteReply", method = RequestMethod.GET)
    public ModelAndView deleteReply(@RequestParam ObjectId replyId) {
        ModelAndView result;
        Assert.notNull(replyService.findOne(replyId.toString()), "La id no puede ser nula");

        Reply res = replyService.findOne(replyId.toString());
        User user = userService.findUserByRepliesContaining(res);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Comment c = commentService.findCommentByRepliesContaining(res);
        Blog b = blogService.findBlogByCommentsContaining(c);

        if(user.equals(userCreated)) {

            Set<Reply> replies1 = c.getReplies();
            replies1.remove(res);
            c.setReplies(replies1);
            commentService.save(c);

            List<Comment> comments1 = b.getComments();
            for(Comment com1: comments1){
                com1.setReplies(replies1);
            }
            b.setComments(comments1);
            blogService.save(b);


            Set<Comment> comments = user.getComments();
            for(Comment com : comments){
                com.setReplies(replies1);
            }
            Set<Reply> replies = user.getReplies();
            replies.remove(res);
            user.setReplies(replies);
            user.setComments(comments);
            userService.saveUser(user);

            replyService.remove(res);
        }

        result = new ModelAndView("redirect:/blog/listReply?blogId=" +b.getId() + "&commentId=" + c.getId());
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
        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("blog/create");
        result.addObject("blogForm", blogForm);
        result.addObject("userCreated", userCreated);
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
