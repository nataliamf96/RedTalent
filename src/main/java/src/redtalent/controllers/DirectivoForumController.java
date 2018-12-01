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
import src.redtalent.forms.CommentForm;
import src.redtalent.forms.ForumForm;
import src.redtalent.forms.ReplyForm;
import src.redtalent.services.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/directivo/forum")
public class DirectivoForumController {

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
    public DirectivoForumController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam ObjectId projectId) {

        ModelAndView result = new ModelAndView("redirect:/403");
        Project project = projectService.findOne(projectId.toString());
        List<Forum> forums = project.getForums();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Collection<Team> teams = teamService.findAll();
        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("directivo/forum/list");
        result.addObject("requestURI", "directivo/forum/list?projectId=" + projectId);
        result.addObject("forums", forums);
        result.addObject("projectId", projectId);
        result.addObject("categories", categories);
        result.addObject("userCreated", userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    // Crear lista de comentarios para el foro

    @RequestMapping(value = "/listComment", method = RequestMethod.GET)
    public ModelAndView listComments(@RequestParam ObjectId forumId, @RequestParam Object projectId) {

        ModelAndView result = new ModelAndView("redirect:/403");
        Collection<Comment> comments;
        Forum forum = forumService.findOne(forumId.toString());

        comments = forum.getComments();
        Collection<Team> teams = teamService.findAll();

        Project project = projectService.findOne(projectId.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("directivo/forum/listComment");
        result.addObject("requestURI", "directivo/forum/listComment?forumId=" + forumId + "&projectId=" + projectId);
        result.addObject("comments", comments);
        result.addObject("forumId", forumId);
        result.addObject("projectId", projectId);
        result.addObject("userCreated", userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

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

        result = new ModelAndView("directivo/forum/listReply");
        result.addObject("requestURI", "directivo/forum/listReply?forumId=" + forumId + "&projectId=" + projectId + "&commentId=" + commentId);
        result.addObject("replies", replies);
        result.addObject("commentId", commentId);
        result.addObject("forumId", forumId);
        result.addObject("projectId", projectId);
        result.addObject("userCreated", userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }
}
