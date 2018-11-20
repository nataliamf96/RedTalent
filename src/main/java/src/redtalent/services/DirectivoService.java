package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.redtalent.domain.Directivo;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.repositories.DirectivoRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class DirectivoService {

    @Autowired
    private DirectivoRepository directivoRepository;

    @Autowired
    private AccountRepository accountRepository;

    public DirectivoService(){
        super();
    }

    public Directivo create(){
        return new Directivo();
    }

    public Directivo findOne(String directivoId){
        Assert.notNull(directivoId,"La directivoId es null");
        Optional<Directivo> result = directivoRepository.findById(directivoId);
        return result.get();
    }

    public Collection<Directivo> findAll(){
        Collection<Directivo> result = directivoRepository.findAll();
        Assert.notNull(result,"La lista de Directivo es NULL");
        return result;
    }

    public Directivo save(Directivo directivo){
        Assert.notNull(directivo,"Ocurrió un error al guardar el Directivo");
        Directivo result = directivoRepository.save(directivo);
        return result;
    }

    public void remove(Directivo directivo){
        Assert.notNull(directivo,"Ocurrió un error al borrar el directivo");
        directivoRepository.delete(directivo);
    }

    public Directivo findByEmail(String email){
        Assert.notNull("email","Email NULL");
        Directivo result = directivoRepository.findByAccount(accountRepository.findByEmail(email));
        return result;
    }

}
