package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Administrator;
import src.redtalent.repositories.AdministratorRepository;

import java.util.Collection;

@Service
@Transactional
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    public AdministratorService(){
        super();
    }

    public Administrator findOne(String adminId){
        Assert.notNull(adminId);
        return administratorRepository.findOne(adminId);
    }

    public Collection<Administrator> findAll(){
        Collection<Administrator> result = administratorRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public Administrator saveUser(Administrator administrator){
        Assert.notNull(administrator);
        administrator.setSuspicious(false);
        administratorRepository.save(administrator);
        return administrator;
    }

    public void remove(Administrator administrator){
        Assert.notNull(administrator);
        administratorRepository.delete(administrator);
    }


}
