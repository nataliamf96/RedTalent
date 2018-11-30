package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.redtalent.domain.Area;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;
import src.redtalent.forms.AreaForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/admin/area")
public class AreaController {

    // Services ---------------------------------------------------------------
    @Autowired
    private AreaService areaService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UtilidadesService utilidadesService;


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
        result.addObject("requestURI", "admin/area/list");
        result.addObject("areas", areas);
        result.addObject("auth",utilidadesService.actorConectado());

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
            result.addObject("requestURI", "./admin/area/create");

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/admin/area/list");
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

                result = new ModelAndView("redirect:/admin/area/list");

            } catch (Throwable oops) {
                result = createModelAndView(areaForm, "No se puede crear correctamente el área");

            }
        }
        return result;
    }


    // Eliminar area --------------

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam ObjectId areaId) {
        ModelAndView result;

        Assert.notNull(areaService.findOne(areaId.toString()), "La id no puede ser nula");

        Area res = areaService.findOne(areaId.toString());

        for(Department d: res.getDepartaments()){
            departmentService.remove(d);
            for(Grade g : d.getGrades()){
                gradeService.remove(g);
            }
        }

        areaService.remove(res);

        result = new ModelAndView("redirect:/admin/area/list");
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
