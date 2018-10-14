package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import src.redtalent.security.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
