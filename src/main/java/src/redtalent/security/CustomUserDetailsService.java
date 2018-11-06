package src.redtalent.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import src.redtalent.domain.*;
import src.redtalent.repositories.*;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private DirectivoRepository directivoRepository;
    @Autowired
    private AccountRepository accountRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByAccount(accountRepository.findByEmail(email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(email);
        User user = userRepository.findByAccount(account);
        Administrator admin = administratorRepository.findByAccount(account);
        Directivo directivo = directivoRepository.findByAccount(account);

        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(account.getRoles());
            return buildUserForAuthentication(user.getAccount(), authorities);
        } else if(admin != null){
            List<GrantedAuthority> authorities = getUserAuthority(account.getRoles());
            return buildAdminForAuthentication(admin.getAccount(), authorities);
        }else if(directivo != null){
            List<GrantedAuthority> authorities = getUserAuthority(account.getRoles());
            return buildDirectivoForAuthentication(directivo.getAccount(), authorities);
        }else{
            throw new UsernameNotFoundException("Email not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(Account account, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }

    private UserDetails buildAdminForAuthentication(Account account, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }

    private UserDetails buildDirectivoForAuthentication(Account account, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }

}
