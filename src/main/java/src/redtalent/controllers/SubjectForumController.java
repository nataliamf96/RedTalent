package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.redtalent.domain.*;
import src.redtalent.forms.AreaForm;
import src.redtalent.forms.CommentForm;
import src.redtalent.forms.SubjectForumForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/subjectForum")
public class SubjectForumController {

    // Services ---------------------------------------------------------------
    @Autowired
    private SubjectForumService subjectForumService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


    // Constructors -----------------------------------------------------------
    public SubjectForumController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<SubjectForum> subjectForums;

        subjectForums = subjectForumService.findAll();

        result = new ModelAndView("subjectForum/list");
        result.addObject("requestURI", "subjectForum/list");
        result.addObject("subjectForums", subjectForums);

        return result;
    }


    // Crear SubjectForum -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        SubjectForumForm subjectForumForm = new SubjectForumForm();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        try {

            result = new ModelAndView("subjectForum/create");
            result.addObject("subjectForumForm", subjectForumForm);
            result.addObject("requestURI", "./subjectForum/create");
            result.addObject("userCreated", userCreated);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/subjectForum/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid SubjectForumForm subjectForumForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        if (binding.hasErrors()) {
            result = createModelAndView(subjectForumForm);
        } else {
            try {
                Assert.notNull(subjectForumForm, "No puede ser nulo el formulario de SubjectForum");

                SubjectForum subjectForum = subjectForumService.create();
                subjectForum.setTitle(subjectForumForm.getTitle());
                subjectForum.setBody(subjectForumForm.getBody());
                SubjectForum saved = this.subjectForumService.save(subjectForum);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());
                Set<SubjectForum> subjectForums = user.getSubjectForums();
                subjectForums.add(saved);
                user.setSubjectForums(subjectForums);
                userService.saveUser(user);

                result = new ModelAndView("redirect:/subjectForum/list");

            } catch (Throwable oops) {
                result = createModelAndView(subjectForumForm, "No se puede crear correctamente los temas del foro");

            }
        }
        return result;
    }

    // Crear lista de comentarios para el foro

    @RequestMapping(value = "/listComment", method = RequestMethod.GET)
    public ModelAndView listComments(@RequestParam ObjectId subjectForumId){

        ModelAndView result;
        Collection<Comment> comments;
        SubjectForum subjectForum = subjectForumService.findOne(subjectForumId.toString());

        comments = subjectForum.getComments();

        result = new ModelAndView("subjectForum/listComment");
        result.addObject("requestURI", "subjectForm/listComment?subjectForumId="+subjectForumId);
        result.addObject("comments", comments);
        result.addObject("subjectForumId", subjectForumId);

        return result;
    }

    // Crear comentarios para el foro

    @RequestMapping(value = "/createComment", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId subjectForumId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        CommentForm commentForm = new CommentForm();
        commentForm.setSubjectForumId(subjectForumId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        try {

            result = new ModelAndView("subjectForum/createComment");
            result.addObject("commentForm", commentForm);
            result.addObject("subjectForumId", subjectForumId);
            result.addObject("requestURI", "./subjectForum/createComment?subjectForumId=" +subjectForumId);
            result.addObject("userCreated", userCreated);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/subjectForum/list");
        }
        return result;
    }

    @RequestMapping(value = "/createComment", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid CommentForm commentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        commentForm.setSubjectForumId(commentForm.getSubjectForumId());

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

                SubjectForum subjectForum = subjectForumService.findOne(commentForm.getSubjectForumId().toString());
                List<Comment> comments1 = subjectForum.getComments();
                comments1.add(saved);
                subjectForum.setComments(comments1);
                subjectForumService.save(subjectForum);

                result = new ModelAndView("redirect:/subjectForum/commentList?subjectForumId=" +commentForm.getSubjectForumId());

            } catch (Throwable oops) {
                result = createCommentModelAndView(commentForm, "No se puede crear correctamente los comentarios del foro");

            }
        }
        return result;
    }

    // Crear respuestas para los comentarios del foro



    // Model and View ---------------

    protected ModelAndView createModelAndView(SubjectForumForm subjectForumForm) {
        ModelAndView result;
        result = createModelAndView(subjectForumForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(SubjectForumForm subjectForumForm, String message) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("subjectForum/create");
        result.addObject("subjectForumForm", subjectForumForm);
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

        result = new ModelAndView("subjectForum/createComment");
        result.addObject("commentForm", commentForm);
        result.addObject("subjectForumId", commentForm.getSubjectForumId());
        result.addObject("userCreated", userCreated);
        result.addObject("message", message);
        return result;
    }

}
