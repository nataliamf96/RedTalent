package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Evaluation;
import src.redtalent.repositories.EvaluationRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    public EvaluationService() {
        super();
    }

    public Evaluation create(){
        Evaluation evaluation = new Evaluation();
        return evaluation;
    }

    public Evaluation findOne(String evaluationId) {
        Assert.notNull(evaluationId, "Evaluation Service : id null");
        Optional<Evaluation> result = evaluationRepository.findById(evaluationId);
        return result.get();
    }

    public List<Evaluation> findAll() {
        List<Evaluation> result = evaluationRepository.findAll();
        Assert.notNull(result, "Evaluation Service : list null");
        return result;
    }

    public Evaluation save(Evaluation evaluation) {
        Assert.notNull(evaluation, "Evaluation Service : Objeto null");
        Evaluation result = evaluationRepository.save(evaluation);
        return result;
    }

    public void remove(Evaluation evaluation) {
        Assert.notNull(evaluation, "Evaluation Service : Objeto null");
        evaluationRepository.delete(evaluation);
    }
}