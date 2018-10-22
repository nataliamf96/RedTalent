package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Application;
import src.redtalent.repositories.ApplicationRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public ApplicationService(){
        super();
    }

    public Application create(){
        Application result = new Application();
        java.util.Date fecha = new Date();
        result.setMoment(fecha);
        result.setStatus("PENDING");
        return result;
    }

    public Application findOne(String applicationId){
        Assert.notNull(applicationId,"Application Service : id null");
        Optional<Application> result = applicationRepository.findById(applicationId);
        return result.get();
    }

    public Collection<Application> findAll(){
        Collection<Application> result = applicationRepository.findAll();
        Assert.notNull(result,"Application Service : list null");
        return result;
    }

    public Application save(Application application){
        Assert.notNull(application,"Application Service : Objeto null");
        return applicationRepository.save(application);
    }

    public void remove(Application application){
        Assert.notNull(application,"Application Service : Objeto null");
        applicationRepository.delete(application);
    }

}
