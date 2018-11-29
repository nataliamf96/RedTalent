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
import src.redtalent.forms.CurriculumForm;
import src.redtalent.forms.EditPasswordForm;
import src.redtalent.forms.UpdateUserForm;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Autowired
    private TagService tagService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ForumService forumService;

    public UserController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        result = new ModelAndView("user/index");
        result.addObject("projects",projectService.findAllByPrivadoFalseAndEstadoFalse());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",utilidadesService.userConectado(authentication.getName()));
        result.addObject("users",userService.findAll());
        result.addObject("usuariosMejorValorados",userService.usuariosOrdenadosPorEvaluacion());
        return result;
    }

    @RequestMapping(value = "/users")
    public ModelAndView users() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("user/users");
        result.addObject("users",userService.findAll());
        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/cambiarContraseña")
    public ModelAndView cambiarContraseña(){
        ModelAndView result;
        EditPasswordForm editPasswordForm;
        editPasswordForm = new EditPasswordForm();

        result = new ModelAndView("user/cambiarContraseña");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("editPasswordForm",editPasswordForm);
        return result;
    }

    @RequestMapping(value = "/cambiarContraseña", method = RequestMethod.POST, params = "saveNuevaContraseña")
    public ModelAndView save(@Valid EditPasswordForm editPasswordForm, BindingResult binding) {
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());

        if (binding.hasErrors()){
            result = createEditModelAndViewChangePassword(editPasswordForm);
        }else if(!bCryptPasswordEncoder.matches(editPasswordForm.getPassword(),user.getAccount().getPassword())){
            result = createEditModelAndViewChangePassword(editPasswordForm,"La contraseña es incorrecta");
        }else if(!editPasswordForm.getNewPassword().equals(editPasswordForm.getConfNewPassword())){
            result = createEditModelAndViewChangePassword(editPasswordForm,"Confirmación de Nueva Contraseña Erronea");
        }else{
            try {
                Account a = accountRepository.findByEmail(authentication.getName());
                a.setPassword(bCryptPasswordEncoder.encode(editPasswordForm.getNewPassword()));
                Account save = accountRepository.save(a);
                user.setAccount(save);
                userService.saveUser(user);
                result = new ModelAndView("redirect:/user/index");
            } catch (Throwable oops) {
                result = createEditModelAndViewChangePassword(editPasswordForm, "ERROR AL CAMBIAR LA CONTRASEÑA");
            }
        }
        return result;
    }

    @RequestMapping(value = "/userData")
    public ModelAndView userData() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        Set<Project> projectsParticipo = new HashSet<Project>();
        Set<Project> projectsCreados = user.getProjects();
        Set<Team> teamsParticipo = new HashSet<Team>();
        Set<Team> teamsCreados = user.getTeams();
        projectsParticipo.addAll(utilidadesService.todosLosProyectosEnLosQueEstoyAceptado(user));
        teamsParticipo.addAll(utilidadesService.todosLosEquiposEnLosQueEstoyAceptado(user));

        for(Project p:projectsParticipo){
            if(p.getCerrado() == true){
                projectsParticipo.remove(p);
            }
        }

        for(Project p:projectsCreados){
            if(p.getCerrado() == true){
                projectsCreados.remove(p);
            }
        }

        result = new ModelAndView("user/userData");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",user);
        result.addObject("users",userService.findAll());
        result.addObject("projectsParticipo", projectsParticipo);
        result.addObject("projectsCreados", projectsCreados);
        result.addObject("teamsParticipo", teamsParticipo);
        result.addObject("teamsCreados", teamsCreados);
        return result;
    }

    @RequestMapping(value = "/dataUser")
    public ModelAndView dataUser(@RequestParam String userId) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("user/dataUser");
        try{
            User user = userService.findOne(userId.toString());
            Set<Project> projectsParticipo = new HashSet<Project>();
            Set<Project> projectsCreados = user.getProjects();
            Set<Team> teamsParticipo = new HashSet<Team>();
            Set<Team> teamsCreados = user.getTeams();
            projectsParticipo.addAll(utilidadesService.todosLosProyectosEnLosQueEstoyAceptado(user));
            teamsParticipo.addAll(utilidadesService.todosLosEquiposEnLosQueEstoyAceptado(user));

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

            result = new ModelAndView("user/userData");
            result.addObject("auth",utilidadesService.actorConectado());
            result.addObject("user",user);
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

    //GET--------------------------------------------------------------
    @RequestMapping(value = "/updateUser", method = RequestMethod.GET)
    public ModelAndView updateUser() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());
        UpdateUserForm updateUserForm;
        updateUserForm = new UpdateUserForm();
        result = updateEditModelAndViewUser(updateUserForm);
        String etiquetas = "";
        Integer primero = 0;
        for(Tag t:user.getTags()){
            if(primero == 0){
                etiquetas = etiquetas + t.getName();
                primero = 1;
            }else{
                etiquetas = etiquetas + "," + t.getName();
            }
        }
        result = new ModelAndView("user/updateUser");
        result.addObject("etiquetas",etiquetas);
        result.addObject("updateUserForm",user);
        result.addObject("userId",user.getId());
        return result;
    }

        @RequestMapping(value = "/updateUser", method = RequestMethod.POST, params = "saveModUser")
        public ModelAndView updateUser(@Valid UpdateUserForm updateUserForm, BindingResult binding){
            ModelAndView result;

            if (binding.hasErrors())
                result = updateEditModelAndViewUser(updateUserForm);
            else
                try {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    User user = utilidadesService.userConectado(authentication.getName());
                    user.setFullname(updateUserForm.getFullname());
                    user.setImage(updateUserForm.getImage());

                    /* Etiquetas */
                    Set<Tag> tagsUser = new HashSet<Tag>();
                    for(String nombreTag : updateUserForm.getEtiquetas().split(",")){
                        Tag tt = null;
                        for(Tag t:tagService.findAll()){
                            if(t.getName().toUpperCase().equals(nombreTag.toUpperCase())){
                                tt = t;
                            }
                        }
                        if(tt == null){
                            tt = tagService.create();
                            tt.setName(nombreTag.replace(" ",""));
                            Tag save = tagService.save(tt);
                            tagsUser.add(save);
                        }else{
                            tagsUser.add(tt);
                        }
                    }
                    user.setTags(tagsUser);
                    userService.saveUser(user);
                    result = new ModelAndView("redirect:/user/index");

                } catch (Throwable oops) {
                    result = updateEditModelAndViewUser(updateUserForm, "ERROR AL ACTUALIZAR EL USUARIO");
                }

            return result;
        }


    @RequestMapping(value = "/filtrarBlogsCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarBlogsCategorias() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("user/filtrarBlogsCategorias");
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
        result = new ModelAndView("user/filtrarBlogsCategoriasResultado");
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

        result = new ModelAndView("user/filtrarProyectosCategoriasResultado");

        if(category.isEmpty()){
            result.addObject("projects",projectService.findAll());
        }else{
            result.addObject("projects",projectService.findProjectsByCategorie_Name(category));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/filtrarProyectosCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarProyectosCategorias() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("user/filtrarProyectosCategorias");
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

        result = new ModelAndView("user/filtrarPerfilDepartamento");
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

        result = new ModelAndView("user/filtrarPerfilGrado");
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

        result = new ModelAndView("user/filtrarPerfilArea");
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

    @RequestMapping(value = "/filtrarForumsCategorias", method = RequestMethod.GET)
    public ModelAndView filtrarForumsCategorias(@RequestParam(value = "category", defaultValue = "") String category) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("user/filtrarForumsCategorias");

        if(category.isEmpty()){
            result.addObject("forums",forumService.findAll());
        }else{
            result.addObject("forums",forumService.findForumsByCategory_Name(category));
        }

        result.addObject("auth",utilidadesService.actorConectado());
        return result;
    }

    @RequestMapping(value = "/filtrarPerfilesPorEtiquetas", method = RequestMethod.GET)
    public ModelAndView filtrarPerfilesPorEtiquetas(@RequestParam(value = "tag", defaultValue = "") String tag) {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        result = new ModelAndView("user/filtrarPerfilesPorEtiquetas");

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

    @RequestMapping(value = "/curriculum/createCurriculum")
    public ModelAndView createCurriculum(){
        ModelAndView result;
        CurriculumForm curriculumForm;
        curriculumForm = new CurriculumForm();

        result = new ModelAndView("user/curriculum/createCurriculum");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("grades",gradeService.findAll());
        result.addObject("curriculumForm",curriculumForm);
        return result;
    }

    @RequestMapping(value = "/curriculum/updateCurriculum")
    public ModelAndView updateCurriculum(){
        ModelAndView result;
        CurriculumForm curriculumForm;
        curriculumForm = new CurriculumForm();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        result = new ModelAndView("user/curriculum/updateCurriculum");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("grades",gradeService.findAll());
        result.addObject("curriculumForm",user.getCurriculum());
        return result;
    }

    @RequestMapping(value = "/curriculum/createCurriculum", method = RequestMethod.POST, params = "createCurriculum")
    public ModelAndView createCurriculum(@Valid CurriculumForm curriculumForm, BindingResult binding){
        ModelAndView result;

        if (binding.hasErrors())
            result = createEditModelAndViewUserCurriculum(curriculumForm);
        else if(curriculumForm.getGrade().equals("0")){
            result = createEditModelAndViewUserCurriculum(curriculumForm,"Seleccione un Grado");
        }else
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = utilidadesService.userConectado(authentication.getName());

                Curriculum curriculum = curriculumService.create();

                curriculum.setUrlLinkedin(curriculumForm.getUrlLinkedin());
                curriculum.setGrade(gradeService.findOne(curriculumForm.getGrade()));
                curriculum.setDescription(curriculumForm.getDescription());
                curriculum.setRealized(true);
                Curriculum csave = curriculumService.save(curriculum);

                user.setCurriculum(csave);
                userService.saveUser(user);

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = createEditModelAndViewUserCurriculum(curriculumForm, "ERROR AL CREAR EL CURRICULUM");
            }

        return result;
    }

    @RequestMapping(value = "/curriculum/updateCurriculum", method = RequestMethod.POST, params = "updateCurriculum")
    public ModelAndView updateCurriculum(@Valid CurriculumForm curriculumForm, BindingResult binding){
        ModelAndView result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        if (binding.hasErrors())
            result = updateEditModelAndViewUserCurriculum(curriculumForm);
        else if(user.getCurriculum().getRealized() == false){
            result = new ModelAndView("redirect:/user/index");
        } else if(curriculumForm.getGrade().equals("0")){
            result = updateEditModelAndViewUserCurriculum(curriculumForm,"Seleccione un Grado");
        }else
            try {

                Curriculum curriculum = user.getCurriculum();

                curriculum.setUrlLinkedin(curriculumForm.getUrlLinkedin());
                curriculum.setGrade(gradeService.findOne(curriculumForm.getGrade()));
                curriculum.setDescription(curriculumForm.getDescription());
                Curriculum csave = curriculumService.save(curriculum);

                user.setCurriculum(csave);
                userService.saveUser(user);

                result = new ModelAndView("redirect:/user/index");

            } catch (Throwable oops) {
                result = updateEditModelAndViewUserCurriculum(curriculumForm, "ERROR AL ACTUALIZAR EL CURRICULUM");
            }

        return result;
    }

    protected ModelAndView updateEditModelAndViewUserCurriculum(CurriculumForm curriculumForm) {
        ModelAndView result;
        result = updateEditModelAndViewUserCurriculum(curriculumForm, null);
        return result;
    }

    protected ModelAndView updateEditModelAndViewUserCurriculum(CurriculumForm curriculumForm, String message) {
        ModelAndView result;

        result = new ModelAndView("user/curriculum/updateCurriculum");
        result.addObject("curriculumForm", curriculumForm);
        result.addObject("message", message);
        result.addObject("grades",gradeService.findAll());
        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

    protected ModelAndView createEditModelAndViewUserCurriculum(CurriculumForm curriculumForm) {
        ModelAndView result;
        result = createEditModelAndViewUserCurriculum(curriculumForm, null);
        return result;
    }

    protected ModelAndView createEditModelAndViewUserCurriculum(CurriculumForm curriculumForm, String message) {
        ModelAndView result;

        result = new ModelAndView("user/curriculum/createCurriculum");
        result.addObject("curriculumForm", curriculumForm);
        result.addObject("message", message);
        result.addObject("grades",gradeService.findAll());
        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }


        protected ModelAndView updateEditModelAndViewUser(UpdateUserForm updateUserForm) {
            ModelAndView result;
            result = updateEditModelAndViewUser(updateUserForm, null);
            return result;
        }

        protected ModelAndView updateEditModelAndViewUser(UpdateUserForm updateUserForm, String message) {
            ModelAndView result;

            result = new ModelAndView("user/updateUser");
            result.addObject("updateUserForm", updateUserForm);
            result.addObject("message", message);
            result.addObject("auth",utilidadesService.actorConectado());

            return result;
        }

        protected ModelAndView createEditModelAndViewChangePassword(EditPasswordForm editPasswordForm) {
            ModelAndView result;
            result = createEditModelAndViewChangePassword(editPasswordForm, null);
            return result;
        }

        protected ModelAndView createEditModelAndViewChangePassword(EditPasswordForm editPasswordForm, String message) {
            ModelAndView result;

            result = new ModelAndView("user/cambiarContraseña");
            result.addObject("editPasswordForm", editPasswordForm);
            result.addObject("message", message);
            result.addObject("auth",utilidadesService.actorConectado());

            return result;
        }

    }


