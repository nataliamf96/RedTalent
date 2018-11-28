package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Recomendation;
import src.redtalent.domain.Reply;
import src.redtalent.repositories.CommentRepository;
import src.redtalent.repositories.RecomendationRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RecomendationService {

    @Autowired
    private RecomendationRepository recomendationRepository;

    public RecomendationService() {
        super();
    }

    public Recomendation create(){
        Recomendation recomendation;

        recomendation = new Recomendation();
        Date date = new Date(System.currentTimeMillis() - 1000);
        recomendation.setMoment(date);

        return recomendation;
    }

    public Recomendation findOne(String recomendationId) {
        Assert.notNull(recomendationId, "Recomendation Service : id null");
        Optional<Recomendation> result = recomendationRepository.findById(recomendationId);
        return result.get();
    }

    public List<Recomendation> findAll() {
        List<Recomendation> result = recomendationRepository.findAll();
        Assert.notNull(result, "Recomendation Service : list null");
        return result;
    }

    public Recomendation save(Recomendation recomendation) {
        Assert.notNull(recomendation, "Recomendation Service : Objeto null");
        Recomendation result = recomendationRepository.save(recomendation);
        return result;
    }

    public void remove(Recomendation recomendation) {
        Assert.notNull(recomendation, "Recomendation Service : Objeto null");
        recomendationRepository.delete(recomendation);
    }
}
