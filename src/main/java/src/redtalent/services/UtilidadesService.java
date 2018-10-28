package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import src.redtalent.domain.Administrator;
import src.redtalent.domain.Application;
import src.redtalent.domain.Team;
import src.redtalent.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilidadesService {

    @Autowired
    private UserService userService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private DirectivoService directivoService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TeamService teamService;

    public UtilidadesService(){
        super();
    }

    public List<User> usuariosDelEquipo(Team team){
        List<User> result = new ArrayList<User>();
        result.add(userService.findUserByTeamsConstains(team));
        team.getApplications().stream().filter(x->x.getStatus().equals("ACCEPTED")).forEach(x->result.add(userService.findUserByApplicationsContains(x)));
        return result;
    }

    public List<String> allEmails(){
        List<String> result = new ArrayList<String>();
        List<String> a = userService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        List<String> b = administratorService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        List<String> c = directivoService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        result.addAll(a);
        result.addAll(b);
        result.addAll(c);
        return result;
    }

    public User userConectado(String email){
        User result = userService.findByEmail(email);
        return result;
    }

    public Boolean estaEnElEquipo(User user, Team team){
        Boolean result = false;

        if(user.getTeams().contains(team)){
            result = true;
        }else{
            for(Application application:user.getApplications()){
                if(team.getApplications().contains(application)){
                    if(application.getStatus().equals("ACCEPTED")){
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Application tieneSolicitudEnviada(User user, Team team){
        Application result = null;
            for(Application application:user.getApplications()){
                if(team.getApplications().contains(application)){
                        result = application;
                        break;
                }
            }
        return result;
    }

    public Administrator adminConectado(String email){
        Administrator result = administratorService.findByEmail(email);
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