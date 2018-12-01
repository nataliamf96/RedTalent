package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.*;
import src.redtalent.forms.BlogForm;
import src.redtalent.forms.CommentForm;
import src.redtalent.forms.ReplyForm;
import src.redtalent.services.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/directivo/blog")
public class DirectivoBlogController {

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
    public DirectivoBlogController() {
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

        result = new ModelAndView("directivo/blog/list");
        result.addObject("requestURI", "directivo/blog/list");
        result.addObject("blogs", blogs);
        result.addObject("categories", categories);
        result.addObject("userCreated", userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

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

        result = new ModelAndView("directivo/blog/listComment");
        result.addObject("requestURI", "directivo/blog/listComment?blogId="+blogId);
        result.addObject("comments", comments);
        result.addObject("blogId", blogId);
        result.addObject("userCreated", userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

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

        result = new ModelAndView("directivo/blog/listReply");
        result.addObject("requestURI", "directivo/blog/listReply?blogId=" + blogId + "&commentId=" +commentId);
        result.addObject("replies", replies);
        result.addObject("commentId", commentId);
        result.addObject("blogId", blogId);
        result.addObject("userCreated", userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }
}
