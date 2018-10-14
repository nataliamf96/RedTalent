package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Area;

public interface AreaRepository extends MongoRepository<Area, String> {

}
