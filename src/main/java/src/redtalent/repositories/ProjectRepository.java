package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.*;

import java.util.Set;

public interface ProjectRepository extends MongoRepository<Project, String> {

    Set<Project> findAllByPrivadoFalseAndEstadoFalse();
    Set<Project> findAllByPrivadoTrue();
    Set<Project> findAllByCategorie(Category category);
    Project findProjectByForumsContains(Forum Forum);
    Set<Project> findAllByCategorieName(String category);
    Set<Project> findProjectsByCategorie_Name(String text);
    Project findProjectByCommentsContains(Comment comment);
}
