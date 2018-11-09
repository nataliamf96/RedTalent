package src.redtalent.domain;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Document(collection = "Accounts")
public class Account extends DomainEntity{

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    @Size(min = 5, max = 32)
    private String password;
    @NotNull
    private boolean enabled;
    @DBRef
    private Set<Role> roles;

    public Account(String email, @Size(min = 5, max = 32) String password, @NotNull boolean enabled, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Account(){
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
