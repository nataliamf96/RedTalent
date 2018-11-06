package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.*;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserByProjectsContains(Project project);
    User findUserByTeamsContains(Team team);
    User findUserByTeams(Team team);
    User findUserByApplicationsContains(Application application);
    User findUserByCommentsContaining(Comment comment);
    User findByAccount(Account account);
}
