package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Actor;
import src.redtalent.repositories.ActorRepository;
import src.redtalent.security.UserAccount;
import src.redtalent.security.UserAccountRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    public ActorService() {
        super();
    }

    public Actor findOne(String actorId){
        Assert.notNull(actorId);
        return actorRepository.findOne(actorId);
    }

    public Collection<Actor> findAll(){
        Collection<Actor> result;
        result = actorRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public Actor save(Actor actor){
        Assert.notNull(actor);
        return actorRepository.save(actor);
    }

    public List<String> allEmails(){
        List<String> result = new ArrayList<String>();
        List<String> a = userService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        List<String> b = administratorService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        List<String> c = companyService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        return result;
    }

    public Boolean usernameExist(String username){
        Boolean result = true;
        if(userAccountRepository.findByUsername(username) == null){
            result = false;
        }
        return result;
    }

    public Boolean findByEmailExist(String email){
        return actorRepository.findByEmailExists(email);
    }





}
