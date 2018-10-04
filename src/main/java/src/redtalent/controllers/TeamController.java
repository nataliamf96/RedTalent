package src.redtalent.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.redtalent.domain.Team;
import src.redtalent.services.TeamService;

@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    public TeamController(){
        super();
    }

    @RequestMapping(value = "/teamData", method = RequestMethod.GET)
    public ModelAndView projectDataList(@RequestParam ObjectId teamId) {
        ModelAndView result;
        result = new ModelAndView("team/teamData");
        Team team = teamService.findOne(teamId.toString());

        result.addObject("team",team);
        result.addObject("projects",team.getProjects());
        result.addObject("comments",team.getComments());

        return result;
    }


}
