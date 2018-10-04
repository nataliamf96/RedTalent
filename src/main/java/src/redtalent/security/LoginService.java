package src.redtalent.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

public class LoginService implements UserDetailsService {

    // Managed repository -----------------------------------------------------

    @Autowired
    UserAccountRepository userRepository;


    // Business methods -------------------------------------------------------

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Assert.notNull(username,"username null");

        UserDetails result;

        result = this.userRepository.findByUsername(username);
        Assert.notNull(result,"username null");
        // WARNING: The following sentences prevent lazy initialisation problems!
        Assert.notNull(result.getAuthorities(),"authority null");
        result.getAuthorities().size();

        return result;
    }

    public static UserAccount getPrincipal() {
        UserAccount result;
        SecurityContext context;
        Authentication authentication;
        Object principal;

        // If the asserts in this method fail, then you're
        // likely to have your Tomcat's working directory
        // corrupt. Please, clear your browser's cache, stop
        // Tomcat, update your Maven's project configuration,
        // clean your project, clean Tomcat's working directory,
        // republish your project, and start it over.

        context = SecurityContextHolder.getContext();
        Assert.notNull(context,"context null");
        authentication = context.getAuthentication();
        Assert.notNull(authentication,"authentication null");
        principal = authentication.getPrincipal();
        Assert.isTrue(principal instanceof UserAccount,"principal isTrue");
        result = (UserAccount) principal;
        Assert.notNull(result,"principal null");
        Assert.isTrue(!result.getId().equals(""));

        return result;
    }

}
