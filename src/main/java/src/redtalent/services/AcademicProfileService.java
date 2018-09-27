package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.repositories.AcademicProfileRepository;

import java.util.Collection;

@Service
@Transactional
public class AcademicProfileService {

    @Autowired
    private AcademicProfileRepository academicProfileRepository;

    public AcademicProfileService(){
        super();
    }

    public AcademicProfile findOne(String apId){
        Assert.notNull(apId);
        return academicProfileRepository.findOne(apId);
    }

    public Collection<AcademicProfile> findAll(){
        Collection<AcademicProfile> result = academicProfileRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public AcademicProfile save(AcademicProfile academicProfile){
        Assert.notNull(academicProfile);
        AcademicProfile result = academicProfileRepository.save(academicProfile);
        return result;
    }

    public void remove(AcademicProfile academicProfile){
        Assert.notNull(academicProfile);
        academicProfileRepository.delete(academicProfile);
    }
}
