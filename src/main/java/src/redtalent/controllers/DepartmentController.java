package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Area;
import src.redtalent.domain.Department;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.AreaService;
import src.redtalent.services.DepartmentService;

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
        result.addObject("requestURI", "department/list.html");
        result.addObject("departments", departments);

        return result;
    }

    // DepartmentByArea ----------------



}
