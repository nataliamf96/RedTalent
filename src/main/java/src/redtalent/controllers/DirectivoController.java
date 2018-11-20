package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Account;
import src.redtalent.domain.Directivo;
import src.redtalent.domain.User;
import src.redtalent.forms.DirectivoForm;
import src.redtalent.forms.UpdateDirectivoForm;
import src.redtalent.forms.UpdateUserForm;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.repositories.RoleRepository;
import src.redtalent.security.Role;
import src.redtalent.services.DirectivoService;
import src.redtalent.services.ProjectService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.HashSet;
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
    public ModelAndView dashboardDirectivo() {
        ModelAndView result;

        result = new ModelAndView("directivo/dashboardDirectivo");
        result.addObject("auth",utilidadesService.actorConectado());
        result.addObject("proyectosAbiertos",projectService.findAllByPrivadoFalseAndEstadoFalse());
        result.addObject("proyectosCerrados",projectService.findAllByPrivadoTrue());

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
