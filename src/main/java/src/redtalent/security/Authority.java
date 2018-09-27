package src.redtalent.security;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;

@Document(collection = "Authority")
public class Authority implements GrantedAuthority {

    public Authority() {
        super();
    }

    // Values -----------------------------------------------------------------

    public static final String	ADMIN		= "ADMIN";
    public static final String	USER	    = "USER";
    public static final String	COMPANY	    = "COMPANY";

    private String authority;

    public Authority(String authority){
        this.authority = authority;
    }

    @NotBlank
    @Pattern(regexp = "^" + Authority.ADMIN + "|" + Authority.USER + "|" + Authority.COMPANY + "$")
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(final String authority) {
        this.authority = authority;
    }

    public static Collection<Authority> listAuthorities() {
        Collection<Authority> result;
        Authority authority;

        result = new ArrayList<Authority>();

        authority = new Authority();
        authority.setAuthority(Authority.ADMIN);
        result.add(authority);

        authority = new Authority();
        authority.setAuthority(Authority.USER);
        result.add(authority);

        authority = new Authority();
        authority.setAuthority(Authority.COMPANY);
        result.add(authority);

        return result;
    }

    // Object interface -------------------------------------------------------

    @Override
    public int hashCode() {
        return this.getAuthority().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        boolean result;

        if (this == other)
            result = true;
        else if (other == null)
            result = false;
        else if (!this.getClass().isInstance(other))
            result = false;
        else
            result = (this.getAuthority().equals(((Authority) other).getAuthority()));

        return result;
    }

    @Override
    public String toString() {
        return this.authority;
    }

}
