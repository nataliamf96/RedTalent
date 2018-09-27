package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface StudentRepository extends MongoRepository<Student, String> {

    @Query("{ 'id' : ?0 }")
    Student findOne(int actorId);

}
