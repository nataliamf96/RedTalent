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
import src.redtalent.domain.*;
import src.redtalent.forms.CategoryForm;
import src.redtalent.forms.DepartmentForm;
import src.redtalent.forms.EvaluationForm;
import src.redtalent.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

    // Services ---------------------------------------------------------------
    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private UserService userService;

    // Constructors -----------------------------------------------------------
    public EvaluationController() {
        super();
    }

    // Crear Evaluacion ----------------------------

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam ObjectId userId, final RedirectAttributes redirectAttrs) {
        ModelAndView result;
        EvaluationForm evaluationForm = new EvaluationForm();
        evaluationForm.setUserId(userId);

        result = new ModelAndView("evaluation/userCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("requestURI", "./evaluation/user/create?userId=" +userId);
        result.addObject("userId", userId);

        return result;
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST, params = "save")
    public ModelAndView saveCreate(@Valid EvaluationForm evaluationForm, BindingResult binding, RedirectAttributes redirectAttrs) {

        ModelAndView result;
        evaluationForm.setUserId(evaluationForm.getUserId());

        if (binding.hasErrors()) {
            result = createModelAndView(evaluationForm);
        } else {
            try {
                Assert.notNull(evaluationForm, "No puede ser nulo el formulario");

                Evaluation evaluation = evaluationService.create();
                evaluation.setRate(evaluationForm.getRate());
                Evaluation saved = this.evaluationService.save(evaluation);

                User user = userService.findOne(evaluationForm.getUserId().toString());
                List<Evaluation> evaluations = user.getEvaluationsReceived();
                evaluations.add(saved);
                user.setEvaluationsReceived(evaluations);
                userService.saveUser(user);

                result = new ModelAndView("redirect:/evaluation/user/create?userId=" + evaluationForm.getUserId());

            } catch (Throwable oops) {
                result = createModelAndView(evaluationForm, "No se puede crear correctamente la evaluacion");

            }
        }
        return result;

    }

    // Model and View ---------------

    protected ModelAndView createModelAndView(EvaluationForm evaluationForm) {
        ModelAndView result;
        result = createModelAndView(evaluationForm, null);
        return result;
    }

    protected ModelAndView createModelAndView(EvaluationForm evaluationForm, String message) {
        ModelAndView result;

        result = new ModelAndView("evaluation/userCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("userId", evaluationForm.getUserId());
        result.addObject("message", message);
        return result;
    }

    protected ModelAndView createProjectModelAndView(EvaluationForm evaluationForm) {
        ModelAndView result;
        result = createProjectModelAndView(evaluationForm, null);
        return result;
    }

    protected ModelAndView createProjectModelAndView(EvaluationForm evaluationForm, String message) {
        ModelAndView result;

        result = new ModelAndView("evaluation/userCreate");
        result.addObject("evaluationForm", evaluationForm);
        result.addObject("projectId", evaluationForm.getUserId());
        result.addObject("message", message);
        return result;
    }

}
