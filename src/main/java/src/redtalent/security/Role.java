package src.redtalent.security;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.domain.DomainEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Document(collection = "Role")
public class Role extends DomainEntity {

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String role;

    public Role(String role){
        this.role = role;
    }

    @NotBlank
    @Pattern(regexp = "^USER|ADMIN|DIRECTIVO$")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
