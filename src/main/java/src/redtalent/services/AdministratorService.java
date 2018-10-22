package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Administrator;
import src.redtalent.repositories.AdministratorRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    public AdministratorService(){
        super();
    }

    public Administrator findOne(String adminId){
        Assert.notNull(adminId,"Administrator Service : id null");
        Optional<Administrator> result = administratorRepository.findById(adminId);
        return result.get();
    }

    public Collection<Administrator> findAll(){
        Collection<Administrator> result = administratorRepository.findAll();
        Assert.notNull(result,"Administrator Service : list null");
        return result;
    }

    public Administrator saveUser(Administrator administrator){
        Assert.notNull(administrator,"Administrator Service : Objeto null");
        administratorRepository.save(administrator);
        return administrator;
    }

    public void remove(Administrator administrator){
        Assert.notNull(administrator,"Administrator Service : Objeto null");
        administratorRepository.delete(administrator);
    }

    public Administrator findByEmail(String email){
        Assert.notNull("email","Email NULL");
        Administrator result = administratorRepository.findByEmail(email);
        return result;
    }


}
