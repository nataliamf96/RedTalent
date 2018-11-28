package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Recomendation;
import src.redtalent.domain.Reply;

public interface RecomendationRepository extends MongoRepository<Recomendation, String> {

}
