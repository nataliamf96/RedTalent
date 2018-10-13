package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.redtalent.domain.User;
import src.redtalent.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;

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

    public User saveUser(User user){
        Assert.notNull(user,"Ocurrió un error al guardar el usuario");
        userRepository.save(user);
        return user;
    }

    public void remove(User user){
        Assert.notNull(user,"Ocurrió un error al borrar el usuario");
        userRepository.delete(user);
    }

}
