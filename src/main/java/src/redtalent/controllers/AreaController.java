package src.redtalent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Area;
import src.redtalent.services.AdministratorService;
import src.redtalent.services.AreaService;

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
        result.addObject("requestURI", "area/list.html");
        result.addObject("areas", areas);

        return result;
    }

}
