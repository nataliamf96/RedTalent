package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Project;
import src.redtalent.domain.Team;
import src.redtalent.domain.User;

import java.util.Set;

public interface TeamRepository extends MongoRepository<Team, String> {

    Team findTeamByProjectsContaining(Project project);
    Team findByUserCreated(User user);

}
