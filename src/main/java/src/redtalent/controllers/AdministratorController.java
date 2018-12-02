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
import java.util.ArrayList;
import java.util.HashSet;
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
    @Autowired
    private  AreaService areaService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private ForumService forumService;

    public AdministratorController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/index");

        result.addObject("projects",projectService.findAll());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",utilidadesService.adminConectado(authentication.getName()));
        result.addObject("users",userService.findAll());
        result.addObject("usuariosMejorValorados",userService.usuariosOrdenadosPorEvaluacion());
        return result;
    }

    @RequestMapping(value = "/dataUser")
    public ModelAndView dataUser(@RequestParam String userId) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/dataUser");
        try{
            User user = userService.findOne(userId.toString());
            Administrator userConectado = administratorService.findByEmail(authentication.getName());
            Set<Project> projectsParticipo = new HashSet<Project>();
            Set<Project> projectsCreados = user.getProjects();
            Set<Team> teamsParticipo = new HashSet<Team>();
            Set<Team> teamsCreados = user.getTeams();
            projectsParticipo.addAll(utilidadesService.todosLosProyectosEnLosQueEstoyAceptado(user));
            teamsParticipo.addAll(utilidadesService.todosLosEquiposEnLosQueEstoyAceptado(user));

            Boolean valora = false;

            for(Project p:projectsParticipo){
                if(p.getCerrado() == true || p.getEstado() == true){
                    projectsParticipo.remove(p);
                }
            }

            for(Project p:projectsCreados){
                if(p.getCerrado() == true || p.getEstado() == true){
                    projectsCreados.remove(p);
                }
            }

            result = new ModelAndView("user/dataUser");
            result.addObject("auth",utilidadesService.actorConectado());
            result.addObject("user",user);
            result.addObject("valora",valora);
            result.addObject("userConectado",userConectado);
            result.addObject("users",userService.findAll());
            result.addObject("projectsParticipo", projectsParticipo);
            result.addObject("projectsCreados", projectsCreados);
            result.addObject("teamsParticipo", teamsParticipo);
            result.addObject("teamsCreados", teamsCreados);
        }catch(Throwable oops){
            if(utilidadesService.actorConectado().equals("USER")){
                result = new ModelAndView("redirect:/user/index");
            }else if(utilidadesService.actorConectado().equals("ADMIN")){
                result = new ModelAndView("redirect:/admin/index");
            }else if(utilidadesService.actorConectado().equals("ADMIN")){
                result = new ModelAndView("redirect:/directivo/index");
            }
        }
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

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ModelAndView proyectos() {
        ModelAndView result;
        result = new ModelAndView("admin/projects");

        result.addObject("projects",projectService.findAll());
        result.addObject("auth", utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/deleteProject", method = RequestMethod.GET)
    public ModelAndView borrarProyecto(@RequestParam String projectId) {
        ModelAndView result;

        try{
            Project project = projectService.findOne(projectId);
            User user = userService.findUserByProjectsContains(project);
            project.setCerrado(true);

            List<Alert> listaAlertas = project.getAlerts();
            Alert newAlert = alertService.create();
            String alerta = "[code:bp]El proyecto " +project.getName()+ " ha sido eliminado por contenido inapropiado. Para más información contacte con el administrador del sistema.";
            newAlert.setText(alerta);
            Alert alertSave = alertService.save(newAlert);
            listaAlertas.add(alertSave);
            project.setAlerts(listaAlertas);

            Project savee = projectService.save(project);

            Set<Project> pp = new HashSet<Project>();
            pp.addAll(user.getProjects());
            pp.remove(projectService.findOne(project.getId()));
            pp.add(savee);
            user.setProjects(pp);
            userService.saveUser(user);

            projectService.saveAll(savee);

            result = new ModelAndView("redirect:/admin/projects");
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

    //FILTROS --------------------------------------------

    @RequestMapping(value = "/filtrarBlogsCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarBlogsCategorias() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/filtrarBlogsCategorias");
        List<Category> categories = new ArrayList<Category>();
        List<Integer> cTam = new ArrayList<Integer>();
        for(Category c:categoryService.findAll()){
            categories.add(c);
            cTam.add(blogService.findBlogsByCategory_Name(c.getName()).size());
        }

        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("categories",categories);
        result.addObject("cTam",cTam);
        return result;
    }

    @RequestMapping(value = "/filtrarBlogsCategoriasResultado", method = RequestMethod.GET)
    public ModelAndView filtrarBlogsCategoriasResultado(@RequestParam(value = "category", defaultValue = "") String category) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/filtrarBlogsCategoriasResultado");
        if(category.isEmpty()){
            result.addObject("blogs",blogService.findAll());
        }else{
            result.addObject("blogs",blogService.findBlogsByCategory_Name(category));
        }
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("users",userService.findAll());
        return result;
    }

    @RequestMapping(value = "/filtrarProyectosCategoriasResultado", method = RequestMethod.GET)
    public ModelAndView filtrarProyectosCategoriasResultado(@RequestParam(value = "category", defaultValue = "") String category) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("admin/filtrarProyectosCategoriasResultado");

        if(category.isEmpty()){
            result.addObject("projects",projectService.findAll());
        }else{
            result.addObject("projects",projectService.findProjectsByCategorie_Name(category));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("users",userService.findAll());
        return result;
    }

    @RequestMapping(value = "/filtrarForumsCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarForumsCategorias() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/filtrarForumsCategorias");
        List<Category> categories = new ArrayList<Category>();
        List<Integer> cTam = new ArrayList<Integer>();
        for(Category c:categoryService.findAll()){
            categories.add(c);
            cTam.add(forumService.findForumsByCategory_Name(c.getName()).size());
        }
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("categories",categories);
        result.addObject("cTam",cTam);
        return result;
    }

    @RequestMapping(value = "/filtrarForumsCategoriasResultado", method = RequestMethod.GET)
    public ModelAndView filtrarForumsCategoriasResultado(@RequestParam(value = "category", defaultValue = "") String category) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("admin/filtrarForumsCategoriasResultado");

        if(category.isEmpty()){
            result.addObject("forums",forumService.findAll());
        }else{
            result.addObject("forums",forumService.findForumsByCategory_Name(category));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("users",userService.findAll());
        return result;
    }

    @RequestMapping(value = "/filtrarProyectosCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarProyectosCategorias() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("admin/filtrarProyectosCategorias");
        List<Category> categories = new ArrayList<Category>();
        List<Integer> cTam = new ArrayList<Integer>();
        for(Category c:categoryService.findAll()){
            categories.add(c);
            cTam.add(projectService.findProjectsByCategorie_Name(c.getName()).size());
        }

        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("categories",categories);
        result.addObject("cTam",cTam);
        return result;
    }

    @RequestMapping(value = "/filtrarPerfilDepartamento", method = RequestMethod.GET)
    public ModelAndView filtrarPerfilDepartamento(@RequestParam(value = "departamento", defaultValue = "") String departamento) {
        ModelAndView result;

        result = new ModelAndView("admin/filtrarPerfilDepartamento");
        result.addObject("departments",departmentService.findAll());

        if(departamento.isEmpty() || departamento.equals("0")){
            result.addObject("users",userService.findAll());
        }else{
            Department a = departmentService.findOne(departamento);
            result.addObject("users",utilidadesService.usuariosPorDepartamento(a));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/filtrarPerfilGrado", method = RequestMethod.GET)
    public ModelAndView filtrarPerfilGrado(@RequestParam(value = "grade", defaultValue = "") String grade) {
        ModelAndView result;

        result = new ModelAndView("admin/filtrarPerfilGrado");
        result.addObject("grades",gradeService.findAll());

        if(grade.isEmpty() || grade.equals("0")){
            result.addObject("users",userService.findAll());
        }else{
            Grade g = gradeService.findOne(grade);
            result.addObject("users",userService.findUsersByCurriculum_Grade(g));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/filtrarPerfilArea", method = RequestMethod.GET)
    public ModelAndView filtrarPerfilArea(@RequestParam(value = "area", defaultValue = "") String area) {
        ModelAndView result;

        result = new ModelAndView("admin/filtrarPerfilArea");
        result.addObject("areas",areaService.findAll());

        if(area.isEmpty() || area.equals("0")){
            result.addObject("users",userService.findAll());
        }else{
            Area a = areaService.findOne(area);
            result.addObject("users",utilidadesService.usuariosPorArea(a));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/filtrarPerfilesPorEtiquetas", method = RequestMethod.GET)
    public ModelAndView filtrarPerfilesPorEtiquetas(@RequestParam(value = "tag", defaultValue = "") String tag) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("admin/filtrarPerfilesPorEtiquetas");

        /* Etiquetas */
        Set<Tag> tagsUser = new HashSet<Tag>();
        for(String nombreTag : tag.replace(" ","").split(",")){
            for(Tag t:tagService.findAll()){
                if(t.getName().toUpperCase().equals(nombreTag.toUpperCase())){
                    tagsUser.add(t);
                }
            }
        }

        if(tag.isEmpty()){
            result.addObject("users",userService.findAll());
        }else if(tagsUser.size() == 0){
            result.addObject("users",new HashSet<User>());
        }else{
            result.addObject("users",userService.findAllByTagsContains(tagsUser));
        }

        result.addObject("auth",utilidadesService.actorConectado());
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
