package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.*;
import src.redtalent.repositories.ProjectRepository;
import src.redtalent.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        List<Tag> tags = new ArrayList<Tag>();
        List<Comment> comments = new ArrayList<Comment>();
        List<Alert> alerts = new ArrayList<Alert>();
        List<ProjectMonitoring> projectMonitorings = new ArrayList<ProjectMonitoring>();
        List<User> users = new ArrayList<User>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        users.add(user);
        result.setAlerts(alerts);
        result.setTags(tags);
        result.setComments(comments);
        result.setProjectMonitorings(projectMonitorings);
        result.setUsers(users);
        return result;
    }

    public Project save(Project project){
        Assert.notNull(project,"Project Service : Objeto null");
        return project;
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

    public void remove(Project project){
        Assert.notNull(project,"Project Service : Objeto null");
        projectRepository.delete(project);
    }

}
