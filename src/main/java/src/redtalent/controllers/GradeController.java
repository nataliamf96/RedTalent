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
    private AdministratorService adminService;


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


    // Crear grado -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        GradeForm gradeForm = new GradeForm();

        try {

            Collection<Department> departments = departmentService.findAll();

            result = new ModelAndView("grade/create");
            result.addObject("gradeForm", gradeForm);
            result.addObject("departments", departments);
            result.addObject("requestURI", "./grade/create");

        } catch (Throwable oops) {
            result = new ModelAndView("redirect:/grade/list");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid GradeForm gradeForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        if (binding.hasErrors()) {
            result = createModelAndView(gradeForm);
        } else {
            try {
                Assert.notNull(gradeForm, "No puede ser nulo el gradeForm");

                Grade grade = gradeService.create();
                grade.setName(gradeForm.getName());
                Grade saved = this.gradeService.save(grade);

                Department department = departmentService.findOne(gradeForm.getDepartment().getId());
                List<Grade> grades = new ArrayList<Grade>();
                grades.addAll(department.getGrades());
                grades.add(saved);
                department.setGrades(grades);

                departmentService.save(department);

                result = new ModelAndView("redirect:/grade/list");

            } catch (Throwable oops) {
                result = createModelAndView(gradeForm, "No se puede crear correctamente el grado");

            }
        }
        return result;
    }


    // Eliminar area --------------

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(GradeForm gradeForm, BindingResult binding) {
        ModelAndView result;

        try {
            Assert.notNull(gradeService.findOne(gradeForm.getGradeId().toString()), "La id no puede ser nula");
            Grade res = gradeService.findOne(gradeForm.getGradeId().toString());

            gradeService.remove(res);

            result = new ModelAndView("redirect:/grade/list.do");
        } catch (Throwable oops) {
            result = createModelAndView(gradeForm, "No se puede eliminar correctamente");
        }

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
        result.addObject("message", message);
        result.addObject("departments", departments);
        return result;
    }


}

