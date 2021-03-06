package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.*;

import java.util.List;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserByProjectsContains(Project project);
    User findUserByTeamsContains(Team team);
    User findUserByTeams(Team team);
    User findUsersByApplicationsContaining(Application application);
    User findUserByCommentsContaining(Comment comment);
    User findByAccount(Account account);
    User findUserByBlogsContains(Blog blog);
    User findUserByRepliesContaining(Reply reply);
    User findUserByCommentsContains(Comment comment);
    User findUserByForumsContains(Forum forum);
    User findUserByCommentsReceivedContains(Comment comment);
    Set<User> findAllByFullnameContains(String texto);
    Set<User> findAllByTagsContains(Set<Tag> listaTags);
    Set<User> findAllByProjectsContains(Project project);
    Set<User> findUsersByCurriculum_Grade(Grade grade);
    User findUserByRecomendationsContains(Recomendation recomendation);
    User findUserByRecomendationsReceivedContains(Recomendation recomendation);
}
