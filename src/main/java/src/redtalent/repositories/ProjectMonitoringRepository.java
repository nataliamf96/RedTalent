package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.ProjectMonitoring;

public interface ProjectMonitoringRepository extends MongoRepository<ProjectMonitoring, String> {
}
