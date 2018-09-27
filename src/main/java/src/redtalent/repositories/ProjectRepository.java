package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
