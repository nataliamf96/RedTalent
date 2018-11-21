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
import src.redtalent.domain.Account;
import src.redtalent.domain.Project;
import src.redtalent.domain.Tag;
import src.redtalent.domain.User;
import src.redtalent.forms.EditPasswordForm;
import src.redtalent.forms.EtiquetasUser;
import src.redtalent.forms.UpdateUserForm;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.HashSet;
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

    public UserController(){
        super();
    }

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        result = new ModelAndView("user/index");
        result.addObject("projects",projectService.findAllByPrivadoFalseAndEstadoFalse());
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",utilidadesService.userConectado(authentication.getName()));
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
        //Team team = teamService.findByUserCreated(user);
        Set<Project> projects = user.getProjects();
        projects.addAll(utilidadesService.todosLosProyectosEnLosQueEstoy(user));
        result = new ModelAndView("user/userData");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("user",user);
        //result.addObject("team",team);
        result.addObject("projects", projects);
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


