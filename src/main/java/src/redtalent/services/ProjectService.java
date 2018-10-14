package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Project;
import src.redtalent.repositories.ProjectRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectService(){
        super();
    }

    public Project create(Project project){
        Assert.notNull(project,"Project Service : id null");
        return project;
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
