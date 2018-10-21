package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Project;
import src.redtalent.domain.User;

import java.util.Set;

public interface ProjectRepository extends MongoRepository<Project, String> {

    Set<Project> findAllByPrivadoFalse();

}
