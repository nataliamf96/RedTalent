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
        List<Tag> tags = new ArrayList<Tag>();
        List<Comment> comments = new ArrayList<Comment>();
        List<Alert> alerts = new ArrayList<Alert>();
        List<ProjectMonitoring> projectMonitorings = new ArrayList<ProjectMonitoring>();
        List<User> users = new ArrayList<User>();
        List<Category> categories = new ArrayList<Category>();
        result.setAlerts(alerts);
        result.setTags(tags);
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

    public void remove(Project project){
        Assert.notNull(project,"Project Service : Objeto null");
        projectRepository.delete(project);
    }

    public Set<Project> findAllByPrivadoFalse(){
        return projectRepository.findAllByPrivadoFalse();
    }
    public Set<Project> findAllByPrivadoTrue(){
        return projectRepository.findAllByPrivadoTrue();
    }

    public Page<Project> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Project> list;
        List<Project> projects = projectRepository.findAll();

        if (projects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, projects.size());
            list = projects.subList(startItem, toIndex);
        }

        Page<Project> bookPage
                = new PageImpl<Project>(list, PageRequest.of(currentPage, pageSize), projects.size());

        return bookPage;
    }

}
