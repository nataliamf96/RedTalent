package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import groovy.util.Eval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.*;
import src.redtalent.repositories.AccountRepository;
import src.redtalent.repositories.CurriculumRepository;
import src.redtalent.repositories.UserRepository;

import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurriculumService curriculumService;

    public UserService(){
        super();
    }

    public User findOne(String userId){
        Assert.notNull(userId,"La userId es null");
        Optional<User> result = userRepository.findById(userId);
        return result.get();
    }

    public Collection<User> findAll(){
        Collection<User> result = userRepository.findAll();
        Assert.notNull(result,"La lista de usuarios es NULL");
        return result;
    }

    public User create(){
        User result = new User();
        Set<Project> pr = new HashSet<Project>();
        Set<Team> ta = new HashSet<Team>();
        Set<Application> ap = new HashSet<Application>();
        Set<Blog> s = new HashSet<Blog>();
        Set<Reply> r = new HashSet<Reply>();
        Set<Comment> c = new HashSet<Comment>();
        Set<Comment> commentsReceived = new HashSet<>();
        Set<Tag> tags = new HashSet<Tag>();
        List<Evaluation> evaluations = new ArrayList<Evaluation>();
        List<Evaluation> evaluationsReceived = new ArrayList<Evaluation>();
        Curriculum curriculum = curriculumService.create();
        List<Recomendation> recomendations = new ArrayList<>();
        List<Recomendation> recomendationsReceived = new ArrayList<>();
        result.setApplications(ap);
        result.setCommentsReceived(commentsReceived);
        result.setTags(tags);
        result.setTeams(ta);
        result.setProjects(pr);
        result.setBlogs(s);
        result.setComments(c);
        result.setReplies(r);
        result.setEvaluations(evaluations);
        result.setEvaluationsReceived(evaluationsReceived);
        result.setCurriculum(curriculum);
        result.setRecomendations(recomendations);
        result.setRecomendationsReceived(recomendationsReceived);
        return result;
    }

    public User saveUser(User user){
        Assert.notNull(user,"Ocurrió un error al guardar el usuario");
        return userRepository.save(user);
    }

    public void remove(User user){
        Assert.notNull(user,"Ocurrió un error al borrar el usuario");
        userRepository.delete(user);
    }

    public User findByEmail(String email){
        Assert.notNull("email","Email NULL");
        User result =  userRepository.findByAccount(accountRepository.findByEmail(email));
        return result;
    }

    public User findUserByProjectsContains(Project project){
        Assert.notNull(project,"Project NULL");
        User result = userRepository.findUserByProjectsContains(project);
        return result;
    }

    public User findUserByTeamsConstains(Team team){
        Assert.notNull(team,"Team NULL");
        User result = userRepository.findUserByTeamsContains(team);
        return result;
    }

    public User findUserByTeams(Team team){
        Assert.notNull(team,"Team NULL");
        User result = userRepository.findUserByTeams(team);
        return result;
    }

    public User findUserByApplicationsContains(Application application){
        Assert.notNull(application,"Application NULL");
        User result = new User();
        for(User user : userRepository.findAll()){
            if(user.getApplications().contains(application)){
                result = user;
                break;
            }
        }
        return result;
    }

    public User findUserByCommentsContaining(Comment comment){
        Assert.notNull(comment, "Comment null");
        User result = userRepository.findUserByCommentsContaining(comment);
        return result;
    }

    public User findUserByBlogsContains(Blog blog){
        Assert.notNull(blog, "Blog null");
        User result = userRepository.findUserByBlogsContains(blog);
        return result;
    }

    public User findUserByForumsContains(Forum forum){
        Assert.notNull(forum, "Forum null");
        User result = userRepository.findUserByForumsContains(forum);
        return result;
    }

    public User findUserByRepliesContaining(Reply reply){
        Assert.notNull(reply, "Reply null");
        User result = userRepository.findUserByRepliesContaining(reply);
        return result;
    }

    public User findUserByCommentsContains(Comment comment){
        Assert.notNull(comment, "Comment null");
        User result = userRepository.findUserByCommentsContains(comment);
        return result;
    }

    public User findUserByCommentsReceivedContains(Comment comment){
        Assert.notNull(comment, "Comment null");
        User result = userRepository.findUserByCommentsReceivedContains(comment);
        return result;
    }

    public User findUserByRecomendationsContains(Recomendation recomendation){
        Assert.notNull(recomendation, "Recomendation null");
        User result = userRepository.findUserByRecomendationsContains(recomendation);
        return result;
    }

    public User findUserByRecomendationsReceivedContains(Recomendation recomendation){
        Assert.notNull(recomendation, "Recomendation null");
        User result = userRepository.findUserByRecomendationsReceivedContains(recomendation);
        return result;
    }

    public Set<User> findAllByEnabledIsTrue(){
        Set<User> result = new HashSet<User>();
        for(User u:userRepository.findAll()){
            if(u.getAccount().isEnabled()){
                result.add(u);
            }
        }
        return result;
    }

    public Set<User> findAllByEnabledIsFalse(){
        Set<User> result = new HashSet<User>();
        for(User u:userRepository.findAll()){
            if(!u.getAccount().isEnabled()){
                result.add(u);
            }
        }
        return result;

    }

    public Set<User> findUsersByCurriculum_Grade(Grade grade){
        return userRepository.findUsersByCurriculum_Grade(grade);
    }


    public Set<User> findAllByTagsContains(Set<Tag> listaTags){
        return userRepository.findAllByTagsContains(listaTags);
    }

    public Set<User> findAllByFullnameContains(String texto){
        return userRepository.findAllByFullnameContains(texto);
    }

    public Set<User> findAllByProjectsContains(Project project){
        return userRepository.findAllByProjectsContains(project);
    }

    public Double mediaEvaluacionUser(User user){
        Double result = 0.0;
        for(Evaluation eva : user.getEvaluations()){
            result += eva.getRate();
        }

        return result/user.getEvaluations().size();
    }

    public List<User> usuariosOrdenadosPorEvaluacion(){
        List<User> users = new ArrayList<User>(findAll());
        Collections.sort(users, new Comparator<User>() {
            public int compare(User o1, User o2) {
                return Double.compare(mediaEvaluacionUser(o1),(mediaEvaluacionUser(o2)));
            }
        });
        return users;
    }
}
