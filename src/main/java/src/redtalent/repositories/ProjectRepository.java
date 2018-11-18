package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Category;
import src.redtalent.domain.Forum;
import src.redtalent.domain.Project;
import src.redtalent.domain.User;

import java.util.Set;

public interface ProjectRepository extends MongoRepository<Project, String> {

    Set<Project> findAllByPrivadoFalseAndEstadoFalse();
    Set<Project> findAllByPrivadoTrue();
    Set<Project> findAllByCategorie(Category category);
    Project findProjectByForumsContains(Forum Forum);

}
