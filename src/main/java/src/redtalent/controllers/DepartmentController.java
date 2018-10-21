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
import src.redtalent.forms.DepartmentForm;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.AreaService;
import src.redtalent.services.DepartmentService;
import src.redtalent.services.GradeService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    // Services ---------------------------------------------------------------
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private GradeService gradeService;

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
        Area a = areaService.findOne(areaId.toString());

        departments = a.getDepartaments();

        result = new ModelAndView("department/list");
        result.addObject("requestURI", "department/byArea?areaId="+areaId);
        result.addObject("departments", departments);
        result.addObject("areaId", areaId);

        return result;
    }

    // Crear departamento -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId areaId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        DepartmentForm departmentForm = new DepartmentForm();

        departmentForm.setAreaId(areaId);

        try {
            Collection<Area> areas = areaService.findAll();

            result = new ModelAndView("department/create");
            result.addObject("departmentForm", departmentForm);
            result.addObject("areas", areas);
            result.addObject("areaId", areaId);
            result.addObject("requestURI", "./department/create?areaId="+areaId);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/department/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid DepartmentForm departmentForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        departmentForm.setAreaId(departmentForm.getAreaId());

        if (binding.hasErrors()) {
            result = createModelAndView(departmentForm);
        } else {
            try {
                Assert.notNull(departmentForm, "No puede ser nulo el departmentForm");

                Department department = departmentService.create();
                department.setDepartment(departmentForm.getDepartment());
                Department saved = this.departmentService.save(department);

                Area area = areaService.findOne(departmentForm.getAreaId().toString());
                List<Department> departments = new ArrayList<Department>();
                departments.addAll(area.getDepartaments());
                departments.add(saved);
                area.setDepartaments(departments);

                areaService.save(area);

                result = new ModelAndView("redirect:/department/byArea?areaId=" + departmentForm.getAreaId());

            } catch (Throwable oops) {
                result = createModelAndView(departmentForm, "No se puede crear correctamente el departamento");

            }
        }
        return result;
    }

    // Eliminar departamento --------------

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam ObjectId departmentId) {
        ModelAndView result;

        Assert.notNull(departmentService.findOne(departmentId.toString()), "La id no puede ser nula");

        Department res = departmentService.findOne(departmentId.toString());

        Area a = areaService.findAreaByDepartamentsContaining(res);
        List<Department> departments = a.getDepartaments();
        departments.remove(res);
        a.setDepartaments(departments);
        areaService.save(a);

        for(Grade g: res.getGrades()){
            gradeService.remove(g);
        }

        departmentService.remove(res);

        result = new ModelAndView("redirect:/department/list");
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

        Collection<Area> areas = areaService.findAll();

        result = new ModelAndView("department/create");
        result.addObject("departmentForm", departmentForm);
        result.addObject("areaId", departmentForm.getAreaId());
        result.addObject("message", message);
        result.addObject("areas", areas);
        return result;
    }


}
