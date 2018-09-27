package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Grade;
import src.redtalent.repositories.GradeRepository;
import java.util.Collection;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public GradeService(){
        super();
    }

    public Grade findOne(String gradeId){
        Assert.notNull(gradeId);
        Grade result = gradeRepository.findOne(gradeId);
        return result;
    }

    public Collection<Grade> findAll(){
        Collection<Grade> result = gradeRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public Grade save(Grade grade){
        Assert.notNull(grade);
        Grade result = gradeRepository.save(grade);
        return result;
    }

    public void remove(Grade grade){
        Assert.notNull(grade);
        gradeRepository.delete(grade);
    }
}
