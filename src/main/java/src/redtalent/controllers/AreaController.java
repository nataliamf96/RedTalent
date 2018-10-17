package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.redtalent.domain.Area;
import src.redtalent.forms.AreaForm;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.AreaService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/area")
public class AreaController {

    // Services ---------------------------------------------------------------
    @Autowired
    private AreaService areaService;

    @Autowired
    private AdministratorService adminService;


    // Constructors -----------------------------------------------------------
    public AreaController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Area> areas;

        areas = areaService.findAll();

        result = new ModelAndView("area/list");
        result.addObject("requestURI", "area/list");
        result.addObject("areas", areas);

        return result;
    }


    // Crear area -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        AreaForm areaForm = new AreaForm();

        try {

            result = new ModelAndView("area/create");
            result.addObject("areaForm", areaForm);
            result.addObject("requestURI", "./area/create");

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/area/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid AreaForm areaForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        if (binding.hasErrors()) {
            result = createModelAndView(areaForm);
        } else {
            try {
                Assert.notNull(areaForm, "No puede ser nulo el areaForm");

                Area area = areaService.create();
                area.setArea(areaForm.getArea());
                Area saved = this.areaService.save(area);

                result = new ModelAndView("redirect:/area/list");

            } catch (Throwable oops) {
                result = createModelAndView(areaForm, "No se puede crear correctamente el área");

            }
        }
        return result;
    }


    // Eliminar area --------------

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(AreaForm areaForm, BindingResult binding) {
        ModelAndView result;

        try {
            Assert.notNull(areaService.findOne(areaForm.getAreaId().toString()), "La id no puede ser nula");
            Area res = areaService.findOne(areaForm.getAreaId().toString());

            areaService.remove(res);

            result = new ModelAndView("redirect:/area/list.do");
        } catch (Throwable oops) {
            result = createModelAndView(areaForm, "No se puede eliminar correctamente");
        }

        return result;
    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(AreaForm areaForm) {
        ModelAndView result;
        result = createModelAndView(areaForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(AreaForm areaForm, String message) {
        ModelAndView result;

        result = new ModelAndView("area/create");
        result.addObject("areaForm", areaForm);
        result.addObject("message", message);
        return result;
    }

}
