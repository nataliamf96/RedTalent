package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.*;
import src.redtalent.forms.DirectivoForm;
import src.redtalent.forms.UpdateDirectivoForm;
import src.redtalent.forms.UpdateUserForm;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.repositories.RoleRepository;
import src.redtalent.security.Role;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/directivo")
public class DirectivoController {

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DirectivoService directivoService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    public DirectivoController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;

        result = new ModelAndView("directivo/index");
        result.addObject("projects",projectService.findAllByPrivadoFalseAndEstadoFalse());
        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/dashboardDirectivo")
    public ModelAndView dashboardDirectivo(){
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("directivo/dashboardDirectivo");
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

    @RequestMapping(value = "/filtrarBlogsCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarBlogsCategorias() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("directivo/filtrarBlogsCategorias");
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
        result = new ModelAndView("directivo/filtrarBlogsCategoriasResultado");
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

        result = new ModelAndView("directivo/filtrarProyectosCategoriasResultado");

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
        result = new ModelAndView("directivo/filtrarForumsCategorias");
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

        result = new ModelAndView("directivo/filtrarForumsCategoriasResultado");

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
        result = new ModelAndView("directivo/filtrarProyectosCategorias");
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

        result = new ModelAndView("directivo/filtrarPerfilDepartamento");
        result.addObject("departments",departmentService.findAll());

        if(departamento.isEmpty()){
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

        result = new ModelAndView("directivo/filtrarPerfilGrado");
        result.addObject("grades",gradeService.findAll());

        if(grade.isEmpty()){
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

        result = new ModelAndView("directivo/filtrarPerfilArea");
        result.addObject("areas",areaService.findAll());

        if(area.isEmpty()){
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

        result = new ModelAndView("directivo/filtrarPerfilesPorEtiquetas");

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

    @RequestMapping(value = "/verTags")
    public ModelAndView verTags() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("directivo/verTags");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("tags",tagService.findAll());
        return result;
    }

    @RequestMapping(value = "/estadisticas")
    public ModelAndView estadisticas() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("directivo/estadisticas");

        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("usersTrue",userService.findAllByEnabledIsTrue());
        result.addObject("usersFalse",userService.findAllByEnabledIsFalse());
        result.addObject("categoriasNombres",utilidadesService.nombresCategoriasConProyectos());
        result.addObject("categoriasConNumeroProyectos",utilidadesService.numeroProyectosPorCategorias());
        result.addObject("temasBlogNombres",utilidadesService.nombresCategoriasConTemasBlog());
        result.addObject("categoriasConNumeroTemasBlog",utilidadesService.numeroTemasBlogPorCategorias());
        return result;
    }

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/registerDirectivo", method = RequestMethod.GET)
    public ModelAndView registerDirectivo() {
        ModelAndView result;
        DirectivoForm directivoForm;

        directivoForm = new DirectivoForm();
        result = createEditModelAndViewDirectivo(directivoForm);

        return result;
    }

    //POST-----------------------------------------------------------------
    @RequestMapping(value = "/registerDirectivo", method = RequestMethod.POST, params = "saveDirectivo")
    public ModelAndView registerDirectivo(@Valid DirectivoForm directivoForm, BindingResult binding) {
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewDirectivo(directivoForm);
        else if(utilidadesService.allEmails().contains(directivoForm.getEmail())){
            result = createEditModelAndViewDirectivo(directivoForm, "Email Existente");
        } else
            try {
                Directivo u = directivoService.create();
                Account a = new Account();
                a.setEmail(directivoForm.getEmail());
                a.setPassword(bCryptPasswordEncoder.encode(directivoForm.getPassword()));
                Role role = roleRepository.findByRole("DIRECTIVO");
                Set<Role> roles = new HashSet<Role>();
                roles.add(role);
                a.setRoles(roles);
                a.setEnabled(false);

                Account save = accountRepository.save(a);
                u.setAccount(save);
                u.setFullname(directivoForm.getFullname());
                u.setImage(directivoForm.getImage());
                directivoService.save(u);

                result = new ModelAndView("redirect:/admin/index");

            } catch (Throwable oops) {
                result = createEditModelAndViewDirectivo(directivoForm, "ERROR EN EL REGISTRO");
            }

        return result;
    }

    protected ModelAndView createEditModelAndViewDirectivo(DirectivoForm directivoForm) {
        ModelAndView result;
        result = createEditModelAndViewDirectivo(directivoForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewDirectivo(DirectivoForm directivoForm, String message) {
        ModelAndView result;

        result = new ModelAndView("directivo/registerDirectivo");
        result.addObject("directivoForm", directivoForm);
        result.addObject("message", message);
        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

    @RequestMapping(value = "/updateDirectivo", method = RequestMethod.GET)
    public ModelAndView updateDirectivo() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Directivo directivo = utilidadesService.directivoConectado(authentication.getName());
        UpdateDirectivoForm updateDirectivoForm;
        updateDirectivoForm = new UpdateDirectivoForm();
        result = updateEditModelAndViewDirectivo(updateDirectivoForm);

        result = new ModelAndView("directivo/updateDirectivo");
        result.addObject("updateDirectivoForm",directivo);
        result.addObject("userId",directivo.getId());
        return result;
    }

    @RequestMapping(value = "/updateDirectivo", method = RequestMethod.POST, params = "saveModDirectivo")
    public ModelAndView updateDirectivo(@Valid UpdateDirectivoForm updateDirectivoForm, BindingResult binding){
        ModelAndView result;

        if (binding.hasErrors())
            result = updateEditModelAndViewDirectivo(updateDirectivoForm);
        else
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Directivo directivo = utilidadesService.directivoConectado(authentication.getName());
                directivo.setFullname(updateDirectivoForm.getFullname());
                directivo.setImage(updateDirectivoForm.getImage());
                directivoService.save(directivo);
                result = new ModelAndView("redirect:/directivo/index");

            } catch (Throwable oops) {
                result = updateEditModelAndViewDirectivo(updateDirectivoForm, "ERROR AL ACTUALIZAR EL DIRECTIVO");
            }

        return result;
    }

    protected ModelAndView updateEditModelAndViewDirectivo(UpdateDirectivoForm updateDirectivoForm) {
        ModelAndView result;
        result = updateEditModelAndViewDirectivo(updateDirectivoForm, null);
        return result;
    }

    protected ModelAndView updateEditModelAndViewDirectivo(UpdateDirectivoForm updateDirectivoForm, String message) {
        ModelAndView result;

        result = new ModelAndView("directivo/updateDirectivo");
        result.addObject("updateDirectivoForm", updateDirectivoForm);
        result.addObject("message", message);
        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

}
