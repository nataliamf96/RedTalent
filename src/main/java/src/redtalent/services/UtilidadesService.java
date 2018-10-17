package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import src.redtalent.domain.Administrator;
import src.redtalent.domain.User;
import src.redtalent.repositories.AdministratorRepository;
import src.redtalent.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilidadesService {

    @Autowired
    private UserService userService;
    @Autowired
    private AdministratorService administratorService;

    public UtilidadesService(){
        super();
    }

    public List<String> allEmails(){
        List<String> result = new ArrayList<String>();
        List<String> a = userService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        List<String> b = administratorService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        return result;
    }

    public User userConectado(String email){
        User result = userService.findByEmail(email);
        return result;
    }

    public String actorConectado(){
        String res = "";
        //Para saber quién está authenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("PROFESOR".equals(auth.getAuthority()) || "ESTUDIANTE".equals(auth.getAuthority()) ||"EGRESADO".equals(auth.getAuthority())) {
                res = "USER";
            }
            else if ("ADMIN".equals(auth.getAuthority())) {
                res = "ADMIN";
            }
            else if ("DIRECTIVO".equals(auth.getAuthority())) {
                res = "DIRECTIVO";
            }
        }

        return res;
    }

}
