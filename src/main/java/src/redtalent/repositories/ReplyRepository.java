package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Reply;

public interface ReplyRepository extends MongoRepository<Reply, String> {
}
