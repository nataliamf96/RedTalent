package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Project;
import src.redtalent.repositories.ProjectRepository;

import java.util.Collection;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectService(){
        super();
    }

    public Project create(Project project){
        Assert.notNull(project);
        return project;
    }

    public Project save(Project project){
        Assert.notNull(project);
        return project;
    }

    public Project findOne(String projectId){
        Project result = projectRepository.findOne(projectId);
        Assert.notNull(result);
        return result;
    }

    public Collection<Project> findAll(){
        return projectRepository.findAll();
    }

    public void remove(Project project){
        Assert.notNull(project);
        projectRepository.delete(project);
    }

}
