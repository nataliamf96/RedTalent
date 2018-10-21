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
import src.redtalent.forms.GradeForm;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.AreaService;
import src.redtalent.services.DepartmentService;
import src.redtalent.services.GradeService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/grade")
public class GradeController {

    // Services ---------------------------------------------------------------
    @Autowired
    private GradeService gradeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AreaService areaService;


    // Constructors -----------------------------------------------------------
    public GradeController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Grade> grades;

        grades = gradeService.findAll();

        result = new ModelAndView("grade/list");
        result.addObject("requestURI", "grade/list");
        result.addObject("grades", grades);

        return result;
    }

    //DepartmentsByArea -------

    @RequestMapping(value="/byDepartment", method = RequestMethod.GET)
    public ModelAndView byDepartment(@RequestParam ObjectId departmentId, @RequestParam ObjectId areaId){

        ModelAndView result;
        Collection<Grade> grades;
        Department d = departmentService.findOne(departmentId.toString());

        grades = d.getGrades();

        result = new ModelAndView("grade/list");
        result.addObject("requestURI", "grade/byDepartment?departmentId="+departmentId+"&areaId=" +areaId);
        result.addObject("grades", grades);
        result.addObject("departmentId", departmentId);
        result.addObject("areaId", areaId);

        return result;
    }


    // Crear grado -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId departmentId, @RequestParam ObjectId areaId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        GradeForm gradeForm = new GradeForm();
        gradeForm.setDepartmentId(departmentId);
        gradeForm.setAreaId(areaId);

        try {

            Collection<Department> departments = departmentService.findAll();

            result = new ModelAndView("grade/create");
            result.addObject("gradeForm", gradeForm);
            result.addObject("departments", departments);
            result.addObject("departmentId", departmentId);
            result.addObject("areaId", areaId);
            result.addObject("requestURI", "./grade/create?departmentId=" + departmentId + "&areaId=" + areaId);

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/grade/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid GradeForm gradeForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        gradeForm.setDepartmentId(gradeForm.getDepartmentId());
        gradeForm.setAreaId(gradeForm.getAreaId());

        if (binding.hasErrors()) {
            result = createModelAndView(gradeForm);
        } else {
            try {
                Assert.notNull(gradeForm, "No puede ser nulo el gradeForm");

                Grade grade = gradeService.create();
                grade.setName(gradeForm.getName());
                Grade saved = this.gradeService.save(grade);

                Department department = departmentService.findOne(gradeForm.getDepartmentId().toString());
                List<Grade> grades = department.getGrades();
                grades.add(saved);
                departmentService.save(department);

                Area area = areaService.findOne(gradeForm.getAreaId().toString());
                List<Department> departments = area.getDepartaments();
                for(Department d: departments){
                    d.setGrades(grades);
                }
                area.setDepartaments(departments);
                areaService.save(area);

                result = new ModelAndView("redirect:/grade/byDepartment?departmentId=" + gradeForm.getDepartmentId()+"&areaId=" +gradeForm.getAreaId());

            } catch (Throwable oops) {
                result = createModelAndView(gradeForm, "No se puede crear correctamente el grado");

            }
        }
        return result;
    }


    // Eliminar area --------------

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam ObjectId gradeId) {
        ModelAndView result;

        Assert.notNull(gradeService.findOne(gradeId.toString()), "La id no puede ser nula");

        Grade res = gradeService.findOne(gradeId.toString());

        Department d = departmentService.findDepartmentByGradesContaining(res);
        List<Grade> grades = d.getGrades();
        grades.remove(res);
        d.setGrades(grades);
        departmentService.save(d);

        gradeService.remove(res);

        result = new ModelAndView("redirect:/grade/list");
        return result;
    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(GradeForm gradeForm) {
        ModelAndView result;
        result = createModelAndView(gradeForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(GradeForm gradeForm, String message) {
        ModelAndView result;

        Collection<Department> departments = departmentService.findAll();

        result = new ModelAndView("grade/create");
        result.addObject("gradeForm", gradeForm);
        result.addObject("departmentId", gradeForm.getDepartmentId());
        result.addObject("areaId", gradeForm.getAreaId());
        result.addObject("message", message);
        result.addObject("departments", departments);
        return result;
    }


}

