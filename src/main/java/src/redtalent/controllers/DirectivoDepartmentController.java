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
import src.redtalent.forms.DepartmentForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/directivo/department")
public class DirectivoDepartmentController {

    // Services ---------------------------------------------------------------
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AdministratorService adminService;

    @Autowired
    private UtilidadesService utilidadesService;


    // Constructors -----------------------------------------------------------
    public DirectivoDepartmentController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Department> departments;

        departments = departmentService.findAll();

        result = new ModelAndView("directivo/department/list");
        result.addObject("requestURI", "directivo/department/list");
        result.addObject("departments", departments);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

    //DepartmentsByArea -------

    @RequestMapping(value="/byArea", method = RequestMethod.GET)
    public ModelAndView byArea(@RequestParam ObjectId areaId){

        ModelAndView result;
        Collection<Department> departments;
        Area a = areaService.findOne(areaId.toString());

        departments = a.getDepartaments();

        result = new ModelAndView("directivo/department/list");
        result.addObject("requestURI", "directivo/department/byArea?areaId="+areaId);
        result.addObject("departments", departments);
        result.addObject("areaId", areaId);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }

}
