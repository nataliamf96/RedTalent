package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.*;
import src.redtalent.repositories.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){
        super();
    }

    public User findOne(String userId){
        Assert.notNull(userId,"La userId es null");
        Optional<User> result = userRepository.findById(userId);
        return result.get();
    }

    public Collection<User> findAll(){
        Collection<User> result = userRepository.findAll();
        Assert.notNull(result,"La lista de usuarios es NULL");
        return result;
    }

    public User create(){
        User result = new User();
        Set<Project> pr = new HashSet<Project>();
        Set<Team> ta = new HashSet<Team>();
        Set<Application> ap = new HashSet<Application>();
        Set<SubjectForum> s = new HashSet<SubjectForum>();
        result.setApplications(ap);
        result.setTeams(ta);
        result.setProjects(pr);
        result.setSubjectForums(s);
        return result;
    }

    public User saveUser(User user){
        Assert.notNull(user,"Ocurrió un error al guardar el usuario");
        return userRepository.save(user);
    }

    public void remove(User user){
        Assert.notNull(user,"Ocurrió un error al borrar el usuario");
        userRepository.delete(user);
    }

    public User findByEmail(String email){
        Assert.notNull("email","Email NULL");
        User result = userRepository.findByEmail(email);
        return result;
    }

    public User findUserByProjectsContains(Project project){
        Assert.notNull(project,"Project NULL");
        User result = userRepository.findUserByProjectsContains(project);
        return result;
    }

    public User findUserByTeamsConstains(Team team){
        Assert.notNull(team,"Team NULL");
        User result = userRepository.findUserByTeamsContains(team);
        return result;
    }

    public User findUserByTeams(Team team){
        Assert.notNull(team,"Team NULL");
        User result = userRepository.findUserByTeams(team);
        return result;
    }

    public User findUserByApplicationsContains(Application application){
        Assert.notNull(application,"Application NULL");
        User result = new User();
        for(User user : userRepository.findAll()){
            if(user.getApplications().contains(application)){
                result = user;
                break;
            }
        }
        return result;
    }

    public User findUserByCommentsContaining(Comment comment){
        Assert.notNull(comment, "Comment null");
        User result = userRepository.findUserByCommentsContaining(comment);
        return result;
    }

}
