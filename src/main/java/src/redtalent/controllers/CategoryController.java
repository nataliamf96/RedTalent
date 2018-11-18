package src.redtalent.controllers;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.redtalent.domain.*;
import src.redtalent.forms.CategoryForm;
import src.redtalent.forms.CommentForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/category")
public class CategoryController {

    // Services ---------------------------------------------------------------
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private BlogService blogService;

    // Constructors -----------------------------------------------------------
    public CategoryController() {
        super();
    }

    //Listing -------------

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView result;

        Collection<Category> categories = categoryService.findAll();

        result = new ModelAndView("category/list");
        result.addObject("requestURI", "category/list");
        result.addObject("categories", categories);

        return result;
    }

    // Crear Categoria -------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        CategoryForm categoryForm = new CategoryForm();

        result = new ModelAndView("category/create");
        result.addObject("categoryForm", categoryForm);
        result.addObject("requestURI", "./category/create");

        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid CategoryForm categoryForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;

        Collection<Category> categories = categoryService.findAll();
        Assert.notNull(categoryForm, "No puede ser nulo el formulario de Comment");
        Boolean aux = false;

        for(Category c: categories) {
            if (c.getName().equals(categoryForm.getName())) {
                aux = true;
                return new ModelAndView("redirect:/403");
            }
        }

        Category category = categoryService.create();
        category.setName(categoryForm.getName());
        Category saved = categoryService.save(category);
        result = new ModelAndView("redirect:/category/list");

        return result;
    }

    //Eliminar una categoria

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView deleteCategory(@RequestParam ObjectId categoryId) {
        ModelAndView result;
        Assert.notNull(categoryService.findOne(categoryId.toString()), "La id no puede ser nula");

        Category res = categoryService.findOne(categoryId.toString());

        Set<Project> projects = projectService.findAllByCategorie(res);
        for(Project p: projects){
            p.setCategorie(null);
        }

        List<Blog> blogs = blogService.findAllByCategorie(res);
        for(Blog b: blogs){
            b.setCategory(null);
        }

        List<Forum> forums = forumService.findAllByCategory(res);
        for(Forum f: forums){
            f.setCategory(null);
        }

        categoryService.remove(res);

        result = new ModelAndView("redirect:/category/list");
        return result;
    }

}
