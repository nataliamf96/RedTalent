package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import groovy.util.Eval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.*;
import src.redtalent.repositories.ProjectRepository;
import src.redtalent.repositories.UserRepository;

import java.util.*;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    public ProjectService(){
        super();
    }

    public Project create(){
        Project result = new Project();
        List<Comment> comments = new ArrayList<Comment>();
        List<Alert> alerts = new ArrayList<Alert>();
        List<User> users = new ArrayList<User>();
        List<Evaluation> evaluations = new ArrayList<>();
        result.setEvaluations(evaluations);
        result.setAlerts(alerts);
        result.setCerrado(false);
        result.setComments(comments);
        result.setEstado(false);
        return result;
    }

    public Project save(Project project){
        Assert.notNull(project,"Project Service : Objeto null");
        return projectRepository.save(project);
    }

    public void saveAll(Project project){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = utilidadesService.userConectado(authentication.getName());

        Project savee = save(project);

        Set<Project> pp = new HashSet<Project>();
        pp.addAll(user.getProjects());
        pp.remove(findOne(project.getId()));
        pp.add(savee);
        user.setProjects(pp);

        Team tt = null;
        for(Team team:user.getTeams()){
            if(team.getProjects().contains(savee)){
                tt = team;
                List<Project> projectsS = new ArrayList<Project>();
                projectsS.addAll(tt.getProjects());
                projectsS.remove(findOne(project.getId()));
                projectsS.add(savee);
                tt.setProjects(projectsS);
                Team ttSave = teamService.save(tt);

                Set<Team> listaEquiposUser = user.getTeams();
                listaEquiposUser.remove(tt);
                listaEquiposUser.add(ttSave);
                user.setTeams(listaEquiposUser);
            }
        }


        userService.saveUser(user);

        Team team = null;
        for(Team teamm:teamService.findAll()){
            if(teamm.getProjects().contains(savee)){
                team = teamm;
            }
        }

        if(team != null){
            List<Project> ppp = new ArrayList<Project>();
            ppp.addAll(team.getProjects());
            ppp.remove(findOne(project.getId()));
            ppp.add(savee);
            team.setProjects(ppp);
            teamService.save(team);
        }

    }

    public Project findOne(String projectId){
        Assert.notNull(projectId,"Project Service : id null");
        Optional<Project> result = projectRepository.findById(projectId);
        return result.get();
    }

    public Collection<Project> findAll(){
        Collection<Project> result = projectRepository.findAll();
        Assert.notNull(result,"Project Service : list null");
        return result;
    }

    public Set<Project> findAllByPrivadoFalseAndEstadoFalse(){
        return projectRepository.findAllByPrivadoFalseAndEstadoFalse();
    }

    public Set<Project> findAllByPrivadoTrue(){
        return projectRepository.findAllByPrivadoTrue();
    }

    public Set<Project> findAllByCategorie(Category category){
        return projectRepository.findAllByCategorie(category);
    }

    public Project findProjectByForumsContains(Forum forum){
        Project result = projectRepository.findProjectByForumsContains(forum);
        return result;
    }

    public Set<Project> findProjectsByCategorie_Name(String category){
        return projectRepository.findProjectsByCategorie_Name(category);
    }

    public Project findProjectByCommentsContains(Comment comment){
        return projectRepository.findProjectByCommentsContains(comment);
    }

}
