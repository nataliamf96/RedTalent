package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Application;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Evaluation;
import src.redtalent.domain.User;
import src.redtalent.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){
        super();
    }

    public User findOne(String userId){
        Assert.notNull(userId);
        return userRepository.findOne(userId);
    }

    public Collection<User> findAll(){
        Collection<User> result = userRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public User create(){
        User result = new User();
        List<Application> applications = new ArrayList<Application>();
        List<Evaluation> evaluations = new ArrayList<Evaluation>();
        List<Comment> comments = new ArrayList<Comment>();
        result.setApplications(applications);
        result.setEvaluations(evaluations);
        result.setComments(comments);
        result.setAcademicProfile(null);
        return result;
    }

    public User save(User user){
        Assert.notNull(user);
        user.setSuspicious(false);

        userRepository.save(user);
        return user;
    }

    public void remove(User user){
        Assert.notNull(user);
        userRepository.delete(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
