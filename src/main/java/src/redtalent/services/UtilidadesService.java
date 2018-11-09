package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import src.redtalent.domain.*;
import src.redtalent.repositories.AccountRepository;

import java.util.*;
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
    @Autowired
    private AccountRepository accountRepository;

    public UtilidadesService(){
        super();
    }

    public List<User> usuariosDelEquipo(Team team){
        List<User> result = new ArrayList<User>();
        result.add(userService.findUserByTeamsConstains(team));
        team.getApplications().stream().filter(x->x.getStatus().equals("ACCEPTED")).forEach(x->result.add(userService.findUserByApplicationsContains(x)));
        return result;
    }

    public Set<Project> todosLosProyectosEnLosQueEstoy(User user){
        Set<Project> result = new HashSet<Project>();

        for(Team t : teamService.findAll()){
            for(Application a : user.getApplications()){
                if(t.getApplications().contains(a)){
                    Project p = t.getProjects().get(0);
                    result.add(p);
                }
            }
        }

        return result;
    }

    public List<String> allEmails(){
        List<String> result = new ArrayList<String>();
        List<String> a = accountRepository.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        result.addAll(a);
        return result;
    }

    public User userConectado(String email){
        User result = userService.findByEmail(email);
        return result;
    }

    public List<User> ultimosCincoUsuariosRegistrados(){
        Collection<User> allList = userService.findAll();
        List<User> result = new ArrayList<User>();

        try{
            int cont = 1;
            for(User u:allList){
                if(cont == 6){
                    break;
                } else{
                    result.add((User) allList.toArray()[allList.size()-cont]);
                    cont++;
                }
            }
        }catch (Throwable oops){

        }

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