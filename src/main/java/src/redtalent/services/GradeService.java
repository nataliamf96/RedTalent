package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Grade;
import src.redtalent.repositories.GradeRepository;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public GradeService(){
        super();
    }

    public Grade findOne(String gradeId){
        Assert.notNull(gradeId,"Grade Service : id null");
        Optional<Grade> result = gradeRepository.findById(gradeId);
        return result.get();
    }

    public Collection<Grade> findAll(){
        Collection<Grade> result = gradeRepository.findAll();
        Assert.notNull(result,"Grade Service : list null");
        return result;
    }

    public Grade save(Grade grade){
        Assert.notNull(grade,"Grade Service : Objeto null");
        Grade result = gradeRepository.save(grade);
        return result;
    }

    public void remove(Grade grade){
        Assert.notNull(grade,"Grade Service : Objeto null");
        gradeRepository.delete(grade);
    }
}
