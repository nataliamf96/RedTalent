package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.User;
import src.redtalent.repositories.UserRepository;

import java.util.Collection;

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

    public User saveUser(User user){
        Assert.notNull(user);
        user.setSuspicious(false);
        user.setRole("USER");
        userRepository.save(user);
        return user;
    }

    public void remove(User user){
        Assert.notNull(user);
        userRepository.delete(user);
    }

}
