package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Project;
import src.redtalent.domain.Team;
import src.redtalent.domain.User;
import src.redtalent.services.ProjectService;
import src.redtalent.services.TeamService;
import src.redtalent.services.UserService;
import src.redtalent.services.UtilidadesService;

import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private TeamService teamService;

    public UserController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("user/index");
        result.addObject("projects",projectService.findAll());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",utilidadesService.userConectado(authentication.getName()));
        return result;
    }

    @RequestMapping(value = "/userData")
    public ModelAndView userData() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        Team team = teamService.findByUserCreated(user);
        result = new ModelAndView("user/userData");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",user);
        result.addObject("team",team);
        return result;
    }

}
