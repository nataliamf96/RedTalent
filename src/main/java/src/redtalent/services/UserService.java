package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.User;
import src.redtalent.repositories.UserRepository;
import src.redtalent.security.LoginService;
import src.redtalent.security.UserAccount;

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

    public void delete(User user){
        Assert.notNull(user);
        userRepository.delete(user);
    }


    //Methods

    public User findByPrincipal() {
        User a;
        UserAccount userAccount;
        userAccount = LoginService.getPrincipal();
        Assert.notNull(userAccount);
        a = findByUserAccount(userAccount);
        Assert.notNull(a);
        return a;
    }

    public User findByUserAccount(UserAccount userAccount) {
        Assert.notNull(userAccount);
        User a;
        a = userRepository.findByUserAccountId(userAccount.getId());
        return a;
    }

}
