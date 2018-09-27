package src.redtalent;

import groovy.util.Eval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import src.redtalent.domain.*;
import src.redtalent.repositories.*;
import src.redtalent.security.Authority;
import src.redtalent.security.UserAccount;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class DbSeeder implements CommandLineRunner{

    //Repositories -------------------------------------------------
    @Autowired
    private AcademicProfileRepository academicProfileRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private PhaseRepository phaseRepository;
    @Autowired
    private ProjectMonitoringRepository projectMonitoringRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    // Constructor -------------------------------------------------
    public DbSeeder(AcademicProfileRepository academicProfileRepository, AdministratorRepository administratorRepository, AlertRepository alertRepository, ApplicationRepository applicationRepository,
                    CommentRepository commentRepository, CompanyRepository companyRepository, EvaluationRepository evaluationRepository, GradeRepository gradeRepository, PhaseRepository phaseRepository,
                    ProjectRepository projectRepository, ProjectMonitoringRepository projectMonitoringRepository, TagRepository tagRepository, TeamRepository teamRepository, UserRepository userRepository){
        this.academicProfileRepository = academicProfileRepository;
        this.administratorRepository = administratorRepository;
        this.alertRepository = alertRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
        this.companyRepository = companyRepository;
        this.evaluationRepository  = evaluationRepository;
        this.gradeRepository = gradeRepository;
        this.phaseRepository = phaseRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        //Codificación para contraseñas

        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        //Borro los documentos de la BBDD

        academicProfileRepository.deleteAll();
        administratorRepository.deleteAll();
        alertRepository.deleteAll();
        applicationRepository.deleteAll();
        commentRepository.deleteAll();
        companyRepository.deleteAll();
        evaluationRepository.deleteAll();
        gradeRepository.deleteAll();
        phaseRepository.deleteAll();
        projectMonitoringRepository.deleteAll();
        projectRepository.deleteAll();
        tagRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();

        //UserAccount -----------------------------------------

        UserAccount userAccount1 = new UserAccount(
                "admin",
                encoder.encodePassword("admin", null),
                Arrays.asList(new Authority(Authority.ADMIN)),
                false);

        UserAccount userAccount2 =  new UserAccount(
                "user1",
                encoder.encodePassword("user1", null),
                Arrays.asList(new Authority(Authority.USER)),
                false);

        UserAccount userAccount3 = new UserAccount(
                "user2",
                encoder.encodePassword("user2", null),
                Arrays.asList(new Authority(Authority.USER)),
                false);

        UserAccount userAccount4 = new UserAccount(
                "user3",
                encoder.encodePassword("user3", null),
                Arrays.asList(new Authority(Authority.USER)),
                false);

        UserAccount userAccount5 =  new UserAccount(
                "company1",
                encoder.encodePassword("company1", null),
                Arrays.asList(new Authority(Authority.COMPANY)),
                false);

        //Academic Profiles -----------------------------------------

        AcademicProfile academicProfile1 = new AcademicProfile(
                "Ingeniería del Software",
                "Estudiante de 4º curso");

        AcademicProfile academicProfile2 = new AcademicProfile(
                "Ingeniería de Computadores",
                "Estudiante de 3º curso");

        AcademicProfile academicProfile3 = new AcademicProfile(
                "Medicina",
                "Estudiante de 5º curso");

        AcademicProfile academicProfile4 = new AcademicProfile(
                "Filología Inglesa",
                "Estudiante de 3º curso");

        List<AcademicProfile> academicProfiles = Arrays.asList(academicProfile1, academicProfile2, academicProfile3, academicProfile4);
        academicProfileRepository.save(academicProfiles);

        //Grades -----------------------------------------

        Grade grade1 = new Grade(
                "Medicina",
                "Universidad de Sevilla");

        Grade grade2 = new Grade(
                "Ingeniería del Software",
                "Universidad de Sevilla");

        List<Grade> grades = Arrays.asList(grade1, grade2);
        gradeRepository.save(grades);

        //Administrators -----------------------------------------

        Administrator admin = new Administrator(
                "nataliamf96@gmail.com",
                "Natalia",
                "Morato Fernández",
                false,
                userAccount1,
                Arrays.asList(grade1, grade2));

        administratorRepository.save(admin);

        //Alerts -----------------------------------------

        Alert alert1 = new Alert(
                "Reunión en el aula A0.11 en la Facultad de Informática de la Universidad de Sevilla el día 11/12 a las 12:00" );

        Alert alert2 = new Alert(
                "El plazo máximo de entrega será el 19/12");

        List<Alert> alerts = Arrays.asList(alert1, alert2);
        alertRepository.save(alerts);

        // Applications -----------------------------------------

        Date moment = new Date(2018,07,11);
        Date moment2 = new Date(2018, 10, 13);

        Application application1 = new Application(
                moment,
                "PENDING");

        Application application2 = new Application(
                moment,
                "ACCEPTED");

        Application application3 = new Application(
                moment,
                "DENIED");

        Application application4 = new Application(
                moment2,
                "PENDING");

        Application application5 = new Application(
                moment2,
                "ACCEPTED");

        Application application6 = new Application(
                moment2,
                "DENIED");

        List<Application> applications = Arrays.asList(application1, application2, application3, application4, application5, application6);
        applicationRepository.save(applications);

        // Comments -----------------------------------------

        Comment comment1 = new Comment(
                "Buena idea",
                "Opino lo mismo, el proyecto está muy currado",
                moment2,
                null);

        Comment comment2 = new Comment(
                "¡Me encanta!",
                "Este proyecto es increíble",
                moment,
                Arrays.asList(comment1));

        Comment comment3 = new Comment(
                "¡Increíble!",
                "Es muy buena idea, me parece increíble",
                moment,
                null);

        List<Comment> comments = Arrays.asList(comment1, comment2, comment3);
        commentRepository.save(comments);

        // Companies -----------------------------------------

        Company company = new Company(
                "everis@gmail.com",
                "Juan",
                "Perez García",
                false,
                userAccount2);

        companyRepository.save(company);

        // Evaluations -----------------------------------------

        Evaluation evaluation1 = new Evaluation(
                1,
                "No me ha gustado nada la coordinación de este equipo.");

        Evaluation evaluation2 = new Evaluation(
                5,
                "Equipazo para trabajar. ¡Sois increíbles!");

        Evaluation evaluation3 = new Evaluation(
                4,
                "Me encantó participar en este equipo.");

        Evaluation evaluation4 = new Evaluation(
                2,
                "Creo que las participaciones del equipo no han sido equitativas.");

        Evaluation evaluation5 = new Evaluation(
                3,
                "Se puede mejorar");

        List<Evaluation> evaluations = Arrays.asList(evaluation1, evaluation2, evaluation3, evaluation4, evaluation5);
        evaluationRepository.save(evaluations);

        // Project Monitorings -----------------------------------------

        ProjectMonitoring projectMonitoring1 = new ProjectMonitoring(
                "Primer seguimiento",
                "Primer seguimiento del proyecto",
                moment,
                "Document1"
        );

        ProjectMonitoring projectMonitoring2 = new ProjectMonitoring(
                "Segundo seguimiento",
                "Segundo seguimiento del proyecto",
                moment,
                "Document1"
        );

        ProjectMonitoring projectMonitoring3 = new ProjectMonitoring(
                "Tercer seguimiento",
                "Tercer seguimiento del proyecto",
                moment,
                "Document1"
        );

        ProjectMonitoring projectMonitoring4 = new ProjectMonitoring(
                "Primer seguimiento",
                "Primer seguimiento del proyecto",
                moment,
                "Document1"
        );
        ProjectMonitoring projectMonitoring5 = new ProjectMonitoring(
                "Segundo seguimiento",
                "Segundo seguimiento del proyecto",
                moment,
                "Document1"
        );

        List<ProjectMonitoring> projectMonitorings = Arrays.asList(projectMonitoring1, projectMonitoring2, projectMonitoring3, projectMonitoring4, projectMonitoring5);
        projectMonitoringRepository.save(projectMonitorings);

        // Tag -----------------------------------------

        Tag tag1 = new Tag(
                "project"
        );

        Tag tag2 = new Tag(
                "MongoTechnology"
        );

        Tag tag3 = new Tag(
                "team"
        );

        Tag tag4 = new Tag(
                "softwareIngenier"
        );

        Tag tag5 = new Tag(
                "developer"
        );

        List<Tag> tags = Arrays.asList(tag1, tag2, tag3, tag4, tag5);
        tagRepository.save(tags);
    }
}



