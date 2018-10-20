package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.redtalent.domain.Project;
import src.redtalent.domain.User;
import src.redtalent.repositories.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
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
        result.setProjects(pr);
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

}
