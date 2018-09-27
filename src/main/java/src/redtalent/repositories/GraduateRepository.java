package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GraduateRepository extends MongoRepository<Graduate, String> {
    Graduate findByEmail(String email);
}
