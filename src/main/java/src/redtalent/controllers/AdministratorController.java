package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.*;
import src.redtalent.forms.UpdateAdminForm;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private TagService tagService;
    @Autowired
    private AlertService alertService;

    public AdministratorController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("admin/index");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",projectService.findAllByPrivadoFalseAndEstadoFalse());
        result.addObject("admin",utilidadesService.adminConectado(authentication.getName()));
        return result;
    }

    @RequestMapping(value = "/dashboardAdmin")
    public ModelAndView dashboardAdmin() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/dashboardAdmin");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("projects",projectService.proyectosOrdenadosPorEvaluacion());
        result.addObject("users",userService.usuariosOrdenadosPorEvaluacion());
        result.addObject("teams",teamService.findAll());
        result.addObject("evaluationsTeam",utilidadesService.evaluationsTeam());
        result.addObject("evaluationsProject",utilidadesService.evaluationsProject());
        result.addObject("evaluationsUser",utilidadesService.evaluationsUser());
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

    @RequestMapping(value = "/verTags")
    public ModelAndView verTags() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/verTags");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("tags",tagService.findAll());
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
        result.addObject("categoriasNombres",utilidadesService.nombresCategoriasConProyectos());
        result.addObject("categoriasConNumeroProyectos",utilidadesService.numeroProyectosPorCategorias());
        result.addObject("temasBlogNombres",utilidadesService.nombresCategoriasConTemasBlog());
        result.addObject("categoriasConNumeroTemasBlog",utilidadesService.numeroTemasBlogPorCategorias());
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

    @RequestMapping(value = "/deleteTag")
    public ModelAndView deleteTag(@RequestParam String tagId) {
        ModelAndView result;

        Tag tag = tagService.findOne(tagId);
        for(User u:userService.findAll()){
            if(u.getTags().contains(tag)){
                Set<Tag> t = u.getTags();
                t.remove(tag);
                u.setTags(t);
                userService.saveUser(u);
            }
        }

        tagService.remove(tag);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/verTags");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("tags",tagService.findAll());

        return result;
    }

    @RequestMapping(value = "/deleteProject", method = RequestMethod.GET)
    public ModelAndView borrarProyecto(@RequestParam String projectId) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        try{
            Project project = projectService.findOne(projectId);
            project.setCerrado(true);

            List<Alert> listaAlertas = project.getAlerts();
            Alert newAlert = alertService.create();
            String alerta = "[code:bp]El proyecto " +project.getName()+ " ha sido eliminado por contenido inapropiado. Para más información contacte con el administrador del sistema.";
            newAlert.setText(alerta);
            Alert alertSave = alertService.save(newAlert);
            listaAlertas.add(alertSave);
            project.setAlerts(listaAlertas);

            Project savee = projectService.save(project);

            projectService.saveAll(savee);

            result = new ModelAndView("redirect:/user/index");
            result.addObject("auth",utilidadesService.actorConectado());
        }catch (Throwable oops){
            result = new ModelAndView("redirect:/403");
        }

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

    @RequestMapping(value = "/updateAdmin", method = RequestMethod.GET)
    public ModelAndView updateAdmin() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Administrator admin = utilidadesService.adminConectado(authentication.getName());
        UpdateAdminForm updateAdminForm;
        updateAdminForm = new UpdateAdminForm();
        result = updateEditModelAndViewAdmin(updateAdminForm);

        result = new ModelAndView("admin/updateAdmin");
        result.addObject("updateAdminForm",admin);
        result.addObject("userId",admin.getId());
        return result;
    }



    @RequestMapping(value = "/updateAdmin", method = RequestMethod.POST, params = "saveModAdmin")
    public ModelAndView updateAdmin(@Valid UpdateAdminForm updateAdminForm, BindingResult binding){
        ModelAndView result;

        if (binding.hasErrors())
            result = updateEditModelAndViewAdmin(updateAdminForm);
        else
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Administrator admin = utilidadesService.adminConectado(authentication.getName());
                admin.setFullname(updateAdminForm.getFullname());
                admin.setImage(updateAdminForm.getImage());
                administratorService.save(admin);
                result = new ModelAndView("redirect:/admin/index");

            } catch (Throwable oops) {
                result = updateEditModelAndViewAdmin(updateAdminForm, "ERROR AL ACTUALIZAR EL DIRECTIVO");
            }

        return result;
    }

    protected ModelAndView updateEditModelAndViewAdmin(UpdateAdminForm updateAdminForm) {
        ModelAndView result;
        result = updateEditModelAndViewAdmin(updateAdminForm, null);
        return result;
    }

    protected ModelAndView updateEditModelAndViewAdmin(UpdateAdminForm updateAdminForm, String message) {
        ModelAndView result;

        result = new ModelAndView("admin/updateAdmin");
        result.addObject("updateAdminForm", updateAdminForm);
        result.addObject("message", message);
        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

}
