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
import src.redtalent.forms.AreaForm;
import src.redtalent.forms.DepartmentForm;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.AreaService;
import src.redtalent.services.DepartmentService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    // Services ---------------------------------------------------------------
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AdministratorService adminService;


    // Constructors -----------------------------------------------------------
    public DepartmentController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Department> departments;

        departments = departmentService.findAll();

        result = new ModelAndView("department/list");
        result.addObject("requestURI", "department/list");
        result.addObject("departments", departments);

        return result;
    }

    //DepartmentsByArea -------

    @RequestMapping(value="/byArea", method = RequestMethod.GET)
    public ModelAndView byArea(@RequestParam ObjectId areaId){

        ModelAndView result;
        Collection<Department> departments;

        departments = departmentService.findDepartmentsBy(areaId);

        result = new ModelAndView("department/list");
        result.addObject("requestURI", "department/byArea?areaId="+areaId);
        result.addObject("departments", departments);
        result.addObject("areaId", areaId);

        return result;
    }

    // Crear area -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        DepartmentForm departmentForm = new DepartmentForm();

        try {

            result = new ModelAndView("department/create");
            result.addObject("departmentForm", departmentForm);
            result.addObject("requestURI", "./department/create");

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/department/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid DepartmentForm departmentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        if (binding.hasErrors()) {
            result = createModelAndView(departmentForm);
        } else {
            try {
                Assert.notNull(departmentForm, "No puede ser nulo el departmentForm");

                Department department = departmentService.create();
                department.setDepartment(departmentForm.getDepartment());
                Department saved = this.departmentService.save(department);

                result = new ModelAndView("redirect:/department/list");

            } catch (Throwable oops) {
                result = createModelAndView(departmentForm, "No se puede crear correctamente el departamento");

            }
        }
        return result;
    }


    // Eliminar area --------------

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(DepartmentForm departmentForm, BindingResult binding) {
        ModelAndView result;

        try {
            Assert.notNull(departmentService.findOne(departmentForm.getDepartmentId().toString()), "La id no puede ser nula");
            Department res = departmentService.findOne(departmentForm.getDepartmentId().toString());

            departmentService.remove(res);

            result = new ModelAndView("redirect:/area/list.do");
        } catch (Throwable oops) {
            result = createModelAndView(departmentForm, "No se puede eliminar correctamente");
        }

        return result;
    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(DepartmentForm departmentForm) {
        ModelAndView result;
        result = createModelAndView(departmentForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(DepartmentForm departmentForm, String message) {
        ModelAndView result;

        result = new ModelAndView("department/create");
        result.addObject("departmentForm", departmentForm);
        result.addObject("message", message);
        return result;
    }


}
