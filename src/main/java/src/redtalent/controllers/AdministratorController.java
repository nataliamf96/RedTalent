package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Account;
import src.redtalent.domain.Administrator;
import src.redtalent.domain.Project;
import src.redtalent.domain.User;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private UtilidadesService utilidadesService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AccountRepository accountRepository;

    public AdministratorController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("admin/index");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",projectService.findAllByPrivadoFalse());
        result.addObject("admin",utilidadesService.adminConectado(authentication.getName()));
        return result;
    }

    @RequestMapping(value = "/dashboardAdmin")
    public ModelAndView dashboardAdmin() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/dashboardAdmin");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",projectService.findAll());
        result.addObject("users",userService.findAll());
        result.addObject("teams",teamService.findAll());
        result.addObject("categorias",categoryService.findAll());
        result.addObject("admin",utilidadesService.adminConectado(authentication.getName()));
        return result;
    }

    @RequestMapping(value = "/usersView")
    public ModelAndView usersView() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/usersView");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usersTrue",userService.findAllByEnabledIsTrue());
        result.addObject("usersFalse",userService.findAllByEnabledIsFalse());
        return result;
    }

    @RequestMapping(value = "/estadisticas")
    public ModelAndView estadisticas() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/estadisticas");

        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usersTrue",userService.findAllByEnabledIsTrue());
        result.addObject("usersFalse",userService.findAllByEnabledIsFalse());

        /*
         * Crear JSON para recogerlo por JavaScript
         */
        Integer primero = 0;
        String a = "";
        for(Project project: projectService.findAll()){
            if(primero == 0){
                a = a + "\""+project.getName()+"\"";
                primero = 1;
            }else{
                a = a + ","+ "\""+project.getName()+"\"";
            }
        }
        result.addObject("nombreProjects",a);
        return result;
    }

    @RequestMapping(value = "/userBanned")
    public ModelAndView userBanned(@RequestParam String userId) {
        ModelAndView result;

        User userBanear = userService.findOne(userId);
        Account accountBanear = userBanear.getAccount();

        accountBanear.setEnabled(true);
        accountRepository.save(accountBanear);

        userBanear.getAccount().setEnabled(true);
        userService.saveUser(userBanear);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/usersView");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usersTrue",userService.findAllByEnabledIsTrue());
        result.addObject("usersFalse",userService.findAllByEnabledIsFalse());
        return result;
    }

    @RequestMapping(value = "/userNoBanned")
    public ModelAndView userNoBanned(@RequestParam String userId) {
        ModelAndView result;

        User userBanear = userService.findOne(userId);
        Account accountBanear = userBanear.getAccount();

        accountBanear.setEnabled(false);
        accountRepository.save(accountBanear);

        userBanear.getAccount().setEnabled(false);
        userService.saveUser(userBanear);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/usersView");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usersTrue",userService.findAllByEnabledIsTrue());
        result.addObject("usersFalse",userService.findAllByEnabledIsFalse());
        return result;
    }

}
