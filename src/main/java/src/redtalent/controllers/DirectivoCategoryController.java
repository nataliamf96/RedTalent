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
import src.redtalent.domain.Blog;
import src.redtalent.domain.Category;
import src.redtalent.domain.Forum;
import src.redtalent.domain.Project;
import src.redtalent.forms.CategoryForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/directivo/category")
public class DirectivoCategoryController {

    // Services ---------------------------------------------------------------
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UtilidadesService utilidadesService;

    // Constructors -----------------------------------------------------------
    public DirectivoCategoryController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;

        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("directivo/category/list");
        result.addObject("requestURI", "directivo/category/list");
        result.addObject("categories", categories);
        result.addObject("auth", utilidadesService.actorConectado());

        return result;
    }
}
