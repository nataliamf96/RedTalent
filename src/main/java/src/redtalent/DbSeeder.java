package src.redtalent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import src.redtalent.domain.*;
import src.redtalent.repositories.*;
import src.redtalent.security.Role;

import java.util.*;


@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AcademicProfileRepository academicProfileRepository;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CommentRepository commentRepository;
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
    private DirectivoRepository directivoRepository;

    // Constructor -------------------------------------------------
    public DbSeeder(DirectivoRepository directivoRepository, AcademicProfileRepository academicProfileRepository, AdministratorRepository administratorRepository, AlertRepository alertRepository, ApplicationRepository applicationRepository,
                    CommentRepository commentRepository, EvaluationRepository evaluationRepository, GradeRepository gradeRepository, PhaseRepository phaseRepository,
                    ProjectRepository projectRepository, ProjectMonitoringRepository projectMonitoringRepository, TagRepository tagRepository, TeamRepository teamRepository, UserRepository userRepository,
                    RoleRepository roleRepository){
        this.directivoRepository = directivoRepository;
        this.academicProfileRepository = academicProfileRepository;
        this.administratorRepository = administratorRepository;
        this.alertRepository = alertRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
        this.evaluationRepository  = evaluationRepository;
        this.gradeRepository = gradeRepository;
        this.phaseRepository = phaseRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.projectMonitoringRepository = projectMonitoringRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        roleRepository.deleteAll();
        academicProfileRepository.deleteAll();
        administratorRepository.deleteAll();
        alertRepository.deleteAll();
        applicationRepository.deleteAll();
        commentRepository.deleteAll();
        evaluationRepository.deleteAll();
        gradeRepository.deleteAll();
        phaseRepository.deleteAll();
        projectMonitoringRepository.deleteAll();
        projectRepository.deleteAll();
        tagRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
        directivoRepository.deleteAll();

        Role estudiante = new Role("ESTUDIANTE");
        Role profesor = new Role("PROFESOR");
        Role egresado = new Role("EGRESADO");
        Role administrador = new Role("ADMIN");
        Role directivo = new Role("DIRECTIVO");
        List<Role> roles = Arrays.asList(estudiante,profesor,egresado,administrador,directivo);
        roleRepository.saveAll(roles);

        Set<Role> roleEstudiante = new HashSet<Role>();
        roleEstudiante.add(estudiante);
        Set<Role> roleProfesor = new HashSet<Role>();
        roleProfesor.add(profesor);
        Set<Role> roleEgresado = new HashSet<Role>();
        roleEgresado.add(egresado);
        Set<Role> roleAdministrador = new HashSet<Role>();
        roleAdministrador.add(administrador);
        Set<Role> roleDirectivo = new HashSet<Role>();
        roleDirectivo.add(directivo);

        User est1 = new User("user1@user.com",bCryptPasswordEncoder.encode("user1"),"Usuario 1",true,roleEstudiante);
        User est2 = new User("user2@user.com",bCryptPasswordEncoder.encode("user2"),"Usuario 2",true,roleEstudiante);
        User est3 = new User("user3@user.com",bCryptPasswordEncoder.encode("user3"),"Usuario 3",true,roleEstudiante);
        User pro1 = new User("profesor1@profesor.com",bCryptPasswordEncoder.encode("profesor1"),"Profesor 1",true,roleProfesor);
        User pro2 = new User("profesor2@profesor.com",bCryptPasswordEncoder.encode("profesor2"),"Profesor 2",true,roleProfesor);
        User pro3 = new User("profesor3@profesor.com",bCryptPasswordEncoder.encode("profesor3"),"Profesor 3",true,roleProfesor);
        User egr1 = new User("egresado1@egresado.com",bCryptPasswordEncoder.encode("egresado1"),"Egresado 1",true,roleEgresado);
        User egr2 = new User("egresado2@egresado.com",bCryptPasswordEncoder.encode("egresado2"),"Egresado 2",true,roleEgresado);
        User egr3 = new User("egresado3@egresado.com",bCryptPasswordEncoder.encode("egresado3"),"Egresado 3",true,roleEgresado);
        List<User> users = Arrays.asList(est1,est2,est3,pro1,pro2,pro3,egr1,egr2,egr3);
        userRepository.saveAll(users);

        Administrator adm1 = new Administrator("admin@admin.com",bCryptPasswordEncoder.encode("admin"),"admin",true,roleAdministrador);
        administratorRepository.save(adm1);

        Directivo dir1 = new Directivo("directivo1@directivo.com",bCryptPasswordEncoder.encode("directivo1"),"Directivo 1", true, roleDirectivo);
        Directivo dir2 = new Directivo("directivo2@directivo.com",bCryptPasswordEncoder.encode("directivo2"),"Directivo 2", true, roleDirectivo);
        Directivo dir3 = new Directivo("directivo3@directivo.com",bCryptPasswordEncoder.encode("directivo3"),"Directivo 3", true, roleDirectivo);
        List<Directivo> directivos = Arrays.asList(dir1,dir2,dir3);
        directivoRepository.saveAll(directivos);

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
        academicProfileRepository.saveAll(academicProfiles);

        //Grades -----------------------------------------

        Grade grade1 = new Grade(
                "Medicina",
                "Universidad de Sevilla");

        Grade grade2 = new Grade(
                "Ingeniería del Software",
                "Universidad de Sevilla");

        List<Grade> grades = Arrays.asList(grade1, grade2);
        gradeRepository.saveAll(grades);

        //Alerts -----------------------------------------

        Alert alert1 = new Alert(
                "Reunión en el aula A0.11 en la Facultad de Informática de la Universidad de Sevilla el día 11/12 a las 12:00" );

        Alert alert2 = new Alert(
                "El plazo máximo de entrega será el 19/12");

        Alert alert3 = new Alert(
                "El plazo máximo de entrega será el 20/10");

        List<Alert> alerts = Arrays.asList(alert1, alert2, alert3);
        alertRepository.saveAll(alerts);

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
        applicationRepository.saveAll(applications);

        // Comments -----------------------------------------

        Comment comment1 = new Comment(
                "Buena idea",
                "Opino lo mismo, el proyecto está muy currado",
                moment2);

        Comment comment2 = new Comment(
                "¡Me encanta!",
                "Este proyecto es increíble",
                moment);

        Comment comment3 = new Comment(
                "¡Increíble!",
                "Es muy buena idea, me parece increíble",
                moment);

        List<Comment> comments = Arrays.asList(comment1, comment2, comment3);
        commentRepository.saveAll(comments);

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
        evaluationRepository.saveAll(evaluations);

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
        projectMonitoringRepository.saveAll(projectMonitorings);

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
        tagRepository.saveAll(tags);

        // Projects -----------------------------------------

        Project project1 = new Project(
                "El Cubo",
                "https://www.zachpoff.com/site/wp-content/uploads/CTW-2016-logo-1.jpg",
                "Este proyecto consiste en hacer un robot en El Cubo",
                "Se requiere un Ingeniero Informático, un Ingeniero Industrial, un Ingeniero en Robótica y un Ingeniero de Telecomunicaciones",
                4,
                "HIGH",
                moment,
                moment2,
                "Document1",
                Arrays.asList(tag1, tag2),
                Arrays.asList(comment1),
                Arrays.asList(alert1),
                Arrays.asList(projectMonitoring1, projectMonitoring2),
                Arrays.asList(est2,pro2,egr2));

        Project project2 = new Project(
                "Tetrix",
                "http://openspace.orieldavies.org/images/The-Kitchen1.jpg",
                "Este proyecto consiste en hacer el juego del Tetrix mediante programación",
                "Se requiere un Ingeniero Informático, un Ingeniero Industrial, un Ingeniero en Robótica y un Ingeniero de Telecomunicaciones",
                5,
                "LOW",
                moment,
                moment2,
                "Document1",
                Arrays.asList(tag3, tag4),
                Arrays.asList(comment2),
                Arrays.asList(alert2),
                Arrays.asList(projectMonitoring3, projectMonitoring4),
                Arrays.asList(est1,pro1,egr1));

        Project project3 = new Project(
                "Proyecto de arduinos",
                "https://justimagine.azureedge.net/wp-media/2016/10/Why-throwing-tech-700x400-LinkedIn.jpg",
                "Se basa en detectar los sentimientos del ser humano trabajando con arduinos",
                "Se requiere un Ingeniero Informático, un Ingeniero Industrial, un Ingeniero en Robótica y un Ingeniero de Telecomunicaciones",
                6,
                "MEDIUM",
                moment,
                moment2,
                "Document1",
                Arrays.asList(tag5),
                Arrays.asList(comment3),
                Arrays.asList(alert3),
                Arrays.asList(projectMonitoring5),
                Arrays.asList(est3,pro3,egr3));

        List<Project> projects = Arrays.asList(project1, project2, project3);
        projectRepository.saveAll(projects);

        // Team -----------------------------------------

        Team team1 = new Team(
                "Los Comadrejas",
                "Equipo cualificado para los proyectos de ámbito informático",
                moment2,
                4,
                Arrays.asList(application1, application2, application3),
                Arrays.asList(evaluation1, evaluation2),
                est1,
                Arrays.asList(tag1, tag2),
                Arrays.asList(comment1),
                Arrays.asList(project1));

        Team team2 = new Team(
                "SuperTeam",
                "Necesitamos gente aplicada. ¡Nos encanta conocer gente nueva!",
                moment2,
                5,
                Arrays.asList(application4, application5),
                Arrays.asList(evaluation3, evaluation4),
                pro2,
                Arrays.asList(tag3, tag4),
                Arrays.asList(comment2),
                Arrays.asList(project2));

        Team team3 = new Team(
                "Los Winners",
                "Queremos ganar el concurso de idea. Necesitamos gente competente.",
                moment2,
                6,
                Arrays.asList(application6),
                Arrays.asList(evaluation5),
                egr3,
                Arrays.asList(tag4, tag5),
                Arrays.asList(comment3),
                Arrays.asList(project3));

        List<Team> teams = Arrays.asList(team1, team2, team3);
        teamRepository.saveAll(teams);

        // Phase ---------------------------------------------------

        Phase phase1 = new Phase(
                "Fase 1",
                Arrays.asList(team1)
        );

        Phase phase2 = new Phase(
                "Fase 2",
                Arrays.asList(team2)
        );

        Phase phase3 = new Phase(
                "Fase 3",
                Arrays.asList(team3)
        );

        List<Phase> phases = Arrays.asList(phase1, phase2, phase3);
        phaseRepository.saveAll(phases);


    }
}