package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.domain.SubjectForum;

public interface SubjectForumRepository extends MongoRepository<SubjectForum, String> {
}
