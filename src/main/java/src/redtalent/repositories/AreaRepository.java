package src.redtalent.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Area;
import src.redtalent.domain.Department;

import java.util.List;

public interface AreaRepository extends MongoRepository<Area, String> {

    Area findAreaByDepartamentsContaining(Department department);
}