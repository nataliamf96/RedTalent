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
@RequestMapping("/admin/blog")
public class BlogAdminController {

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
    public BlogAdminController() {
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

        result = new ModelAndView("admin/blog/list");
        result.addObject("requestURI", "admin/blog/list");
        result.addObject("blogs", blogs);
        result.addObject("categories", categories);
        result.addObject("userCreated", userCreated);

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

        result = new ModelAndView("admin/blog/listComment");
        result.addObject("requestURI", "admin/blog/listComment?blogId="+blogId);
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

        result = new ModelAndView("admin/blog/listReply");
        result.addObject("requestURI", "blog/listReply?blogId=" + blogId + "&commentId=" +commentId);
        result.addObject("replies", replies);
        result.addObject("commentId", commentId);
        result.addObject("blogId", blogId);
        result.addObject("userCreated", userCreated);

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
        Administrator admin = utilidadesService.adminConectado(authentication.getName());

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

        result = new ModelAndView("redirect:/admin/blog/list");
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
        User user1 = userService.findUserByBlogsContains(b);


            List<Comment> comments1 = b.getComments();
            comments1.remove(res);
            b.setComments(comments1);
            blogService.save(b);

            if(user.equals(user1)){
                Set<Comment> comments = user.getComments();
                comments.remove(res);
                user.setComments(comments);

                Set<Blog> blogs = user.getBlogs();
                for(Blog b1: blogs){
                    if(b1.equals(b)){
                        b1.setComments(comments1);
                    }
                }
                user.setBlogs(blogs);

                userService.saveUser(user);
            } else {

                Set<Comment> comments = user.getComments();
                comments.remove(res);
                user.setComments(comments);
                userService.saveUser(user);

                Set<Blog> blogs = user1.getBlogs();
                for (Blog b1 : blogs) {
                    if(b1.equals(b)){
                        b1.setComments(comments1);
                    }
                }
                user1.setBlogs(blogs);
                userService.saveUser(user1);
            }

            for (Reply r : res.getReplies()) {
                replyService.remove(r);
            }

            commentService.remove(res);


        result = new ModelAndView("redirect:/admin/blog/listComment?blogId=" +b.getId());
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
        User userBlog = userService.findUserByBlogsContains(b);
        User userComent = userService.findUserByCommentsContains(c);

            Set<Reply> replies1 = c.getReplies();
            replies1.remove(res);
            c.setReplies(replies1);
            commentService.save(c);

            List<Comment> comments1 = b.getComments();
            for(Comment com1: comments1){
                if(com1.equals(c)){
                    com1.setReplies(replies1);
                }
            }
            b.setComments(comments1);
            blogService.save(b);

            if(!user.equals(userComent) && !user.equals(userBlog)) {
                Set<Reply> replies = user.getReplies();
                replies.remove(res);
                user.setReplies(replies);
                userService.saveUser(user);

                Set<Comment> comments = userComent.getComments();
                for (Comment com : comments) {
                    if (com.equals(c)) {
                        com.setReplies(replies1);
                    }
                }
                userComent.setComments(comments);
                userService.saveUser(userComent);

                Set<Blog> blogs = userBlog.getBlogs();
                for (Blog b1 : blogs) {
                    for (Comment c1 : b1.getComments()) {
                        if (c1.equals(c)) {
                            c1.setReplies(replies1);
                        }
                    }
                }
                userBlog.setBlogs(blogs);
                userService.saveUser(userBlog);
            } else{
                if(userComent.equals(user) && userBlog.equals(user)){
                    Set<Reply> replies = user.getReplies();
                    replies.remove(res);
                    user.setReplies(replies);

                    Set<Comment> comments = user.getComments();
                    for (Comment com : comments) {
                        if (com.equals(c)) {
                            com.setReplies(replies1);
                        }
                    }
                    user.setComments(comments);

                    Set<Blog> blogs = user.getBlogs();
                    for (Blog b1 : blogs) {
                        for (Comment c1 : b1.getComments()) {
                            if (c1.equals(c)) {
                                c1.setReplies(replies1);
                            }
                        }
                    }
                    user.setBlogs(blogs);
                    userService.saveUser(user);
                }
                if(userBlog.equals(user) && !userComent.equals(user)){

                    Set<Comment> comments = userComent.getComments();
                    for (Comment com : comments) {
                        if (com.equals(c)) {
                            com.setReplies(replies1);
                        }
                    }
                    userComent.setComments(comments);
                    userService.saveUser(userComent);

                    Set<Reply> replies = user.getReplies();
                    replies.remove(res);
                    user.setReplies(replies);

                    Set<Blog> blogs = user.getBlogs();
                    for (Blog b1 : blogs) {
                        for (Comment c1 : b1.getComments()) {
                            if (c1.equals(c)) {
                                c1.setReplies(replies1);
                            }
                        }
                    }
                    user.setBlogs(blogs);
                    userService.saveUser(user);
                }
                if(!userBlog.equals(user) && userComent.equals(user)){

                    Set<Reply> replies = user.getReplies();
                    replies.remove(res);
                    user.setReplies(replies);

                    Set<Comment> comments = user.getComments();
                    for (Comment com : comments) {
                        if (com.equals(c)) {
                            com.setReplies(replies1);
                        }
                    }
                    user.setComments(comments);
                    userService.saveUser(user);

                    Set<Blog> blogs = userBlog.getBlogs();
                    for (Blog b1 : blogs) {
                        for (Comment c1 : b1.getComments()) {
                            if (c1.equals(c)) {
                                c1.setReplies(replies1);
                            }
                        }
                    }
                    userBlog.setBlogs(blogs);
                    userService.saveUser(userBlog);
                }
            }
            replyService.remove(res);

        result = new ModelAndView("redirect:/admin/blog/listReply?blogId=" +b.getId() + "&commentId=" + c.getId());
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
