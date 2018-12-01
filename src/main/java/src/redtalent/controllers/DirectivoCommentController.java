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
import src.redtalent.services.*;

import java.util.*;

@Controller
@RequestMapping("/directivo/comment")
public class DirectivoCommentController {

    // Services ---------------------------------------------------------------
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TeamService teamService;


    // Constructors -----------------------------------------------------------
    public DirectivoCommentController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        Collection<User> users = userService.findAll();
        Set<Comment> comments = new HashSet<>();

        for(User u: users){
           comments.addAll(u.getCommentsReceived());
        }

        result = new ModelAndView("directivo/comment/list");
        result.addObject("requestURI", "directivo/comment/list");
        result.addObject("comments", comments);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    //Lista comment de projects

    @RequestMapping(value="/project/list", method = RequestMethod.GET)
    public ModelAndView listProject() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        List<Comment> comments = new ArrayList<>();

        for(Project project: projectService.findAll()) {
             comments.addAll(project.getComments());
        }

        result = new ModelAndView("directivo/comment/listp");
        result.addObject("requestURI", "directivo/comment/project/list");
        result.addObject("comments", comments);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    //Lista comment de teams

    @RequestMapping(value="/team/list", method = RequestMethod.GET)
    public ModelAndView listTeam() {

        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCreated = utilidadesService.userConectado(authentication.getName());
        List<Comment> comments = new ArrayList<>();

        for(Team team: teamService.findAll()) {
            comments.addAll(team.getComments());
        }

        result = new ModelAndView("directivo/comment/listt");
        result.addObject("requestURI", "directivo/comment/team/list");
        result.addObject("comments", comments);
        result.addObject("userCreated",userCreated);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }
}
