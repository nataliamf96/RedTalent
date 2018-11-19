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
import src.redtalent.services.CommentService;
import src.redtalent.services.ReplyService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/comment")
public class CommentController {

    // Services ---------------------------------------------------------------
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private ReplyService replyService;


    // Constructors -----------------------------------------------------------
    public CommentController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam ObjectId userId) {

        ModelAndView result;
        User user = userService.findOne(userId.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        Set<Comment> comments = user.getCommentsReceived();

        result = new ModelAndView("comment/list");
        result.addObject("requestURI", "comment/list?userId=" +userId);
        result.addObject("comments", comments);
        result.addObject("userId", userId);
        result.addObject("userCreated",userCreated);

        return result;
    }

    // Crear Comentario -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId userId, final RedirectAttributes redirectAttrs) {
        ModelAndView result = new ModelAndView("redirect:/403");
        CommentForm commentForm = new CommentForm();
        commentForm.setUserId(userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        User res = userService.findOne(userId.toString());

        if(!userCreated.equals(res)){
            result = new ModelAndView("comment/create");
            result.addObject("commentForm", commentForm);
            result.addObject("userId", userId);
            result.addObject("requestURI", "./comment/create?userId=" +userId);
            result.addObject("userCreated", userCreated);

        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setUserId(commentForm.getUserId());

        if (binding.hasErrors()) {
            result = createCommentModelAndView(commentForm);
        } else {
            try {
                Assert.notNull(commentForm, "No puede ser nulo el formulario de Comment");

                Comment comment = commentService.create();
                comment.setTitle(commentForm.getTitle());
                comment.setText(commentForm.getText());
                Comment saved = commentService.save(comment);

                User user = userService.findOne(commentForm.getUserId().toString());
                Set<Comment> comments = user.getCommentsReceived();
                comments.add(saved);
                user.setCommentsReceived(comments);
                userService.saveUser(user);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User userCreated = utilidadesService.userConectado(authentication.getName());
                Set<Comment> comments1 = userCreated.getComments();
                comments1.add(saved);
                userCreated.setComments(comments1);
                userService.saveUser(userCreated);

                result = new ModelAndView("redirect:/comment/list?userId=" +commentForm.getUserId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios");

            }
        }
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

        if (userCreador.equals(userCreated)) {
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
        }

        result = new ModelAndView("redirect:/comment/list?userId=" +user.getId());
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
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }
}
