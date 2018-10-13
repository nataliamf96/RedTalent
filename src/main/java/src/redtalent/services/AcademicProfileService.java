package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.repositories.AcademicProfileRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class AcademicProfileService {

    @Autowired
    private AcademicProfileRepository academicProfileRepository;

    public AcademicProfileService(){
        super();
    }

    public AcademicProfile findOne(String apId){
        Assert.notNull(apId,"Academic Profile: id null");
        Optional<AcademicProfile> result = academicProfileRepository.findById(apId);
        return result.get();
    }

    public Collection<AcademicProfile> findAll(){
        Collection<AcademicProfile> result = academicProfileRepository.findAll();
        Assert.notNull(result,"Academic Profile: list null");
        return result;
    }

    public AcademicProfile save(AcademicProfile academicProfile){
        Assert.notNull(academicProfile,"Academic Profile: Objeto null");
        AcademicProfile result = academicProfileRepository.save(academicProfile);
        return result;
    }

    public void remove(AcademicProfile academicProfile){
        Assert.notNull(academicProfile,"Academic Profile: Objeto null");
        academicProfileRepository.delete(academicProfile);
    }
}
