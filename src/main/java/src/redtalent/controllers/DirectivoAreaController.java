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
import src.redtalent.services.AreaService;
import src.redtalent.services.DepartmentService;
import src.redtalent.services.GradeService;
import src.redtalent.services.UtilidadesService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/directivo/area")
public class DirectivoAreaController {

    // Services ---------------------------------------------------------------
    @Autowired
    private AreaService areaService;

    @Autowired
    private UtilidadesService utilidadesService;


    // Constructors -----------------------------------------------------------
    public DirectivoAreaController() {
        super();
    }


    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;
        Collection<Area> areas;

        areas = areaService.findAll();

        result = new ModelAndView("directivo/area/list");
        result.addObject("requestURI", "directivo/area/list");
        result.addObject("areas", areas);
        result.addObject("auth",utilidadesService.actorConectado());

        return result;
    }

}
