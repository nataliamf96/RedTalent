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
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BlogService blogService;

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

    public Directivo directivoConectado(String email){
        Directivo result = directivoService.findByEmail(email);
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

    public String nombreProyectosConComa(){
        Integer primero = 0;
        String a = "";
        for(Project project: projectService.findAll()){
            if(primero == 0){
                a = a + "\""+project.getName()+"\"";
                primero = 1;
            }else{
                a = a + ","+ "\""+project.getName()+"\"";
            }
        }
        return a;
    }

    public String nombresCategoriasConTemasBlog(){
        String result = "";
        Integer primero = 0;
        for(Category category:categoryService.findAll()){
            if(blogService.findAllByCategorie(category).size()!=0){
                if(primero == 0){
                    result = result + "\""+category.getName()+"\"";
                    primero = 1;
                }else{
                    result = result + ","+ "\""+category.getName()+"\"";
                }
            }
        }
        return result;
    }

    public String nombresCategoriasConProyectos(){
        String result = "";
        Integer primero = 0;
        for(Category category:categoryService.findAll()){
            if(projectService.findAllByCategorie(category).size()!=0){
                if(primero == 0){
                    result = result + "\""+category.getName()+"\"";
                    primero = 1;
                }else{
                    result = result + ","+ "\""+category.getName()+"\"";
                }
            }
        }
        return result;
    }

    public String numeroProyectosPorCategorias(){
        String result = "";
        Integer primero = 0;
        for(Category category:categoryService.findAll()){
            if(projectService.findAllByCategorie(category).size()!=0){
                if(primero == 0){
                    result = result + projectService.findAllByCategorie(category).size();
                    primero = 1;
                }else{
                    result = result + ","+projectService.findAllByCategorie(category).size();
                }
            }
        }
        return result;
    }

    public String numeroTemasBlogPorCategorias(){
        String result = "";
        Integer primero = 0;
        for(Category category:categoryService.findAll()){
            if(blogService.findAllByCategorie(category).size()!=0){
                if(primero == 0){
                    result = result + blogService.findAllByCategorie(category).size();
                    primero = 1;
                }else{
                    result = result + ","+blogService.findAllByCategorie(category).size();
                }
            }
        }
        return result;
    }

    public List<Double> evaluationsTeam(){
        List<Double> result = new ArrayList<Double>();
        for(Team t:teamService.findAll()){
            Double sum = 0.0;
            Double media = 0.0;
            if(t.getEvaluations().size()==0){
                result.add(0.0);
            }else{
                for(Evaluation evaluation:t.getEvaluations()){
                    sum += evaluation.getRate();
                }
                media = sum/t.getEvaluations().size();
                result.add(media);
            }
        }
        return result;
    }

}