package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Actor;
import src.redtalent.domain.Administrator;
import src.redtalent.domain.Company;
import src.redtalent.domain.User;
import src.redtalent.services.ActorService;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.CompanyService;
import src.redtalent.services.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController{

    @Autowired
    private ActorService actorService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    public ActorController(){
        super();
    }

    // Listing ----------------------------------------------------------------
    @RequestMapping(value = "/listActors", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        result = new ModelAndView("actor/listActors");

        Collection<User> users = userService.findAll();
        Collection<Administrator> administrators = administratorService.findAll();
        Collection<Company> companies = companyService.findAll();
        Collection<Actor> actors = new ArrayList<Actor>();
        actors.addAll(users);
        actors.addAll(administrators);
        actors.addAll(companies);
        result.addObject("actors",actors);
        result.addObject("requestURI", "actor/listActors.html");

        return result;
    }

}
