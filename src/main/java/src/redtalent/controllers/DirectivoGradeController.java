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
import src.redtalent.forms.GradeForm;
import src.redtalent.services.AreaService;
import src.redtalent.services.DepartmentService;
import src.redtalent.services.GradeService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/directivo/grade")
public class DirectivoGradeController {

    // Services ---------------------------------------------------------------
    @Autowired
    private GradeService gradeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private UtilidadesService utilidadesService;


    // Constructors -----------------------------------------------------------
    public DirectivoGradeController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Grade> grades;

        grades = gradeService.findAll();

        result = new ModelAndView("directivo/grade/list");
        result.addObject("requestURI", "directivo/grade/list");
        result.addObject("grades", grades);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    //DepartmentsByArea -------

    @RequestMapping(value="/byDepartment", method = RequestMethod.GET)
    public ModelAndView byDepartment(@RequestParam ObjectId departmentId, @RequestParam ObjectId areaId){

        ModelAndView result;
        Collection<Grade> grades;
        Department d = departmentService.findOne(departmentId.toString());

        grades = d.getGrades();

        result = new ModelAndView("directivo/grade/list");
        result.addObject("requestURI", "directivo/grade/byDepartment?departmentId="+departmentId+"&areaId=" +areaId);
        result.addObject("grades", grades);
        result.addObject("departmentId", departmentId);
        result.addObject("areaId", areaId);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }
}

