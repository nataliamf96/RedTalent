package src.redtalent.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Actor;
import src.redtalent.repositories.ActorRepository;
import src.redtalent.security.LoginService;
import src.redtalent.security.UserAccount;
import src.redtalent.security.UserAccountRepository;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

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





}
