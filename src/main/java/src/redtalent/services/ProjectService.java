package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ProjectService(){
        super();
    }

    public Project create(){
        Project result = new Project();
        List<Comment> comments = new ArrayList<Comment>();
        List<Alert> alerts = new ArrayList<Alert>();
        List<ProjectMonitoring> projectMonitorings = new ArrayList<ProjectMonitoring>();
        List<User> users = new ArrayList<User>();
        result.setAlerts(alerts);
        result.setCerrado(false);
        result.setComments(comments);
        result.setProjectMonitorings(projectMonitorings);
        result.setEstado(false);
        return result;
    }

    public Project save(Project project){
        Assert.notNull(project,"Project Service : Objeto null");
        return projectRepository.save(project);
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

}
