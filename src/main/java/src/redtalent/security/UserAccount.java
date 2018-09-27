package src.redtalent.security;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import src.redtalent.domain.DomainEntity;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

public class UserAccount extends DomainEntity implements UserDetails {

    // Constructors -----------------------------------------------------------

    private static final long	serialVersionUID	= 7254823034213841482L;

    // Attributes -------------------------------------------------------------

    private Boolean banned;

    // UserDetails interface --------------------------------------------------
    @Indexed(unique = true)
    private String					username;
    private String					password;
    private Collection<Authority> authorities;


    public UserAccount() {
        super();
        this.authorities = new ArrayList<Authority>();
    }

    public UserAccount(String username, String password, Collection<Authority> authorities, Boolean banned){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.banned = banned;
    }

    public boolean getBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Size(min = 5, max = 32)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Size(min = 5, max = 32)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @NotEmpty
    @Valid
    public Collection<Authority> getAuthorities() {
        // WARNING: Should return an unmodifiable copy, but it's not possible with hibernate!
        return this.authorities;
    }

    public void setAuthorities(final Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(final Authority authority) {
        Assert.notNull(authority);
        Assert.isTrue(!this.authorities.contains(authority));

        this.authorities.add(authority);
    }

    public void removeAuthority(final Authority authority) {
        Assert.notNull(authority);
        Assert.isTrue(this.authorities.contains(authority));

        this.authorities.remove(authority);
    }

    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    public boolean isEnabled() {
        return getBanned();
    }

}
