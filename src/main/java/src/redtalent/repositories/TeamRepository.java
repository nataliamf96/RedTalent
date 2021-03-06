package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Application;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Project;
import src.redtalent.domain.Team;

public interface TeamRepository extends MongoRepository<Team, String> {
    Team findTeamByProjectsContaining(Project project);
    Team findTeamByApplicationsContaining(Application application);
    Team findTeamByCommentsContains(Comment comment);
}
