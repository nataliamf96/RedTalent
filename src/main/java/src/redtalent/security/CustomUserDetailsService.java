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
import src.redtalent.repositories.AdministratorRepository;
import src.redtalent.repositories.DirectivoRepository;
import src.redtalent.repositories.RoleRepository;
import src.redtalent.repositories.UserRepository;

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

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        Administrator admin = administratorRepository.findByEmail(email);
        Directivo directivo = directivoRepository.findByEmail(email);

        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else if(admin != null){
            List<GrantedAuthority> authorities = getUserAuthority(admin.getRoles());
            return buildAdminForAuthentication(admin, authorities);
        }else if(directivo != null){
            List<GrantedAuthority> authorities = getUserAuthority(directivo.getRoles());
            return buildDirectivoForAuthentication(directivo, authorities);
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

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    private UserDetails buildAdminForAuthentication(Administrator admin, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(), authorities);
    }

    private UserDetails buildDirectivoForAuthentication(Directivo directivo, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(directivo.getEmail(), directivo.getPassword(), authorities);
    }

}
