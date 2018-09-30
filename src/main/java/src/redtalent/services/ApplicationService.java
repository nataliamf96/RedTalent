package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Application;
import src.redtalent.domain.User;
import src.redtalent.repositories.ApplicationRepository;

import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserService userService;

    public ApplicationService(){
        super();
    }


    public Application create() {
        User u = userService.findByPrincipal();
        Assert.notNull(u);

        Application result = new Application();

        Date moment = new Date(System.currentTimeMillis() - 1000);
        result.setMoment(moment);

        return result;
    }

    public Application findOne(String applicationId){
        Assert.notNull(applicationId);

        Application result = applicationRepository.findOne(applicationId);

        return result;
    }

    public Collection<Application> findAll(){
        Collection<Application> result = applicationRepository.findAll();
        Assert.notNull(result);

        return result;
    }

    public Application save(Application application){
        Assert.notNull(application);

        Application result = applicationRepository.save(application);

        return result;
    }

    public void delete(Application application){
        Assert.notNull(application);

        applicationRepository.delete(application);
    }

}
