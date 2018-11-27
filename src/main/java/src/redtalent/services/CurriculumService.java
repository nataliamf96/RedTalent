package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Curriculum;
import src.redtalent.repositories.CurriculumRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class CurriculumService {

    @Autowired
    private CurriculumRepository curriculumRepository;

    public CurriculumService(){
        super();
    }

    public Curriculum create(){
        Curriculum curriculum;

        curriculum = new Curriculum();
        curriculum.setRealized(false);

        return curriculum;
    }

    public Curriculum findOne(String curriculum){
        Assert.notNull(curriculum,"Curriculum: id null");
        Optional<Curriculum> result = curriculumRepository.findById(curriculum);
        return result.get();
    }

    public Collection<Curriculum> findAll(){
        Collection<Curriculum> result = curriculumRepository.findAll();
        Assert.notNull(result,"Curriculum: list null");
        return result;
    }

    public Curriculum save(Curriculum curriculum){
        Assert.notNull(curriculum,"Curriculum: Objeto null");
        Curriculum result = curriculumRepository.save(curriculum);
        return result;
    }

    public void remove(Curriculum curriculum){
        Assert.notNull(curriculum,"Curriculum: Objeto null");
        curriculumRepository.delete(curriculum);
    }

}
