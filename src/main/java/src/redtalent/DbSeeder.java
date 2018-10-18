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
public class DbSeeder implements CommandLineRunner{

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
    private DepartmentRepository departmentRepository;
    @Autowired
    private AreaRepository areaRepository;
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

    public void run(String... strings) throws Exception {

        roleRepository.deleteAll();
        academicProfileRepository.deleteAll();
        administratorRepository.deleteAll();
        alertRepository.deleteAll();
        applicationRepository.deleteAll();
        areaRepository.deleteAll();
        departmentRepository.deleteAll();
        commentRepository.deleteAll();
        evaluationRepository.deleteAll();
        gradeRepository.deleteAll();
        phaseRepository.deleteAll();
        projectMonitoringRepository.deleteAll();
        tagRepository.deleteAll();
        teamRepository.deleteAll();
        projectRepository.deleteAll();
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

        User est1 = new User("user1@user.com",bCryptPasswordEncoder.encode("user1"),"Usuario 1",true,roleEstudiante,new HashSet<Project>());
        User est2 = new User("user2@user.com",bCryptPasswordEncoder.encode("user2"),"Usuario 2",true,roleEstudiante,new HashSet<Project>());
        User est3 = new User("user3@user.com",bCryptPasswordEncoder.encode("user3"),"Usuario 3",true,roleEstudiante,new HashSet<Project>());
        User pro1 = new User("profesor1@profesor.com",bCryptPasswordEncoder.encode("profesor1"),"Profesor 1",true,roleProfesor,new HashSet<Project>());
        User pro2 = new User("profesor2@profesor.com",bCryptPasswordEncoder.encode("profesor2"),"Profesor 2",true,roleProfesor,new HashSet<Project>());
        User pro3 = new User("profesor3@profesor.com",bCryptPasswordEncoder.encode("profesor3"),"Profesor 3",true,roleProfesor,new HashSet<Project>());
        User egr1 = new User("egresado1@egresado.com",bCryptPasswordEncoder.encode("egresado1"),"Egresado 1",true,roleEgresado,new HashSet<Project>());
        User egr2 = new User("egresado2@egresado.com",bCryptPasswordEncoder.encode("egresado2"),"Egresado 2",true,roleEgresado,new HashSet<Project>());
        User egr3 = new User("egresado3@egresado.com",bCryptPasswordEncoder.encode("egresado3"),"Egresado 3",true,roleEgresado,new HashSet<Project>());
        List<User> users = Arrays.asList(est2,est3,pro2,pro3,egr2,egr3);
        userRepository.saveAll(users);
        User aa = userRepository.save(est1);
        User bb = userRepository.save(pro1);
        User cc = userRepository.save(egr1);

        Administrator adm1 = new Administrator("admin@admin.com",bCryptPasswordEncoder.encode("admin"),"admin",true,roleAdministrador);
        administratorRepository.save(adm1);

        Directivo dir1 = new Directivo("directivo1@directivo.com",bCryptPasswordEncoder.encode("directivo1"),"Directivo 1", true , roleDirectivo);
        Directivo dir2 = new Directivo("directivo2@directivo.com",bCryptPasswordEncoder.encode("directivo2"),"Directivo 2", true , roleDirectivo);
        Directivo dir3 = new Directivo("directivo3@directivo.com",bCryptPasswordEncoder.encode("directivo3"),"Directivo 3", true , roleDirectivo);
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

        //Grades ----------------------------------------
        Grade grade1 = new Grade(
                "Doble Grado en Adminstración y Dirección de Empresas y en Derecho");

        Grade grade2 = new Grade(
                "Doble Grado en Derecho y Economía");

        Grade grade3 = new Grade(
                "Doble Grado en Derecho y en Finanzas y Contabilidad");

        Grade grade4 = new Grade(
                "Doble Grado en Derecho y Gestión y Administración Pública");

        Grade grade5 = new Grade(
                "Doble Grado en Educación Primaria y Estudios Franceses");

        Grade grade6 = new Grade(
                "Doble Grado en Farmacia y Óptica y Optometría");

        Grade grade7 = new Grade(
                "Doble Grado en Filología Clásica y Filolofía Hispánica");

        Grade grade8 = new Grade(
                "Doble Grado en Finanzas y Contabilidad y Relaciones Laborales y Recursos Humanos");

        Grade grade9 = new Grade(
                "Doble Grado en Física y en Ingeniería de Materiales");

        Grade grade10 = new Grade(
                "Doble Grado en Física y Matemáticas");

        Grade grade11 = new Grade(
                "Doble Grado en Fisioterapia y Ciencias de la Actividad Física y del Deporte");

        Grade grade12 = new Grade(
                "Doble Grado en Geografía y Gestión del Territorio e Historia");

        Grade grade13 = new Grade(
                "Doble Grado en Ing.en Diseño Ind.y Desarrollo del Producto e Ing. Mecánica");

        Grade grade14 = new Grade(
                "Doble Grado en Ingeniería Agrícola y Grado Ciencias Ambientales");

        Grade grade15 = new Grade(
                "Doble Grado en Ingeniería Eléctrica e Ingeniería Electrónica Industrial");

        Grade grade16 = new Grade(
                "Doble Grado en Ingeniería Eléctrica e Ingeniería Mecánica");

        Grade grade17 = new Grade(
                "Doble Grado en Ingeniería Informática - Tecnologías Informáticas y Matemáticas");

        Grade grade18 = new Grade(
                "Doble Grado en Lengua y Literatura Alemanas y en Educación Primaria");

        Grade grade19 = new Grade(
                "Doble Grado en Matemáticas y Estadística");

        Grade grade20 = new Grade(
                "Doble Grado en Periodismo y Comunicación Audiovisual");

        Grade grade21 = new Grade(
                "Doble Grado en Química y en Ingeniería de Materiales");

        Grade grade22 = new Grade(
                "Grado en Administración y Dirección de Empresas");

        Grade grade23 = new Grade(
                "Grado en Antropología Social y Cultural");

        Grade grade24 = new Grade(
                "Grado en Arqueología");

        Grade grade25 = new Grade(
                "Grado en Arquitectura");

        Grade grade26 = new Grade(
                "Grado en Bellas Artes");

        Grade grade27 = new Grade(
                "Grado en Biología");

        Grade grade28 = new Grade(
                "Grado en Biomedicina Básica y Experimental");

        Grade grade29 = new Grade(
                "Grado en Bioquímica");

        Grade grade30 = new Grade(
                "Grado en Ciencias de la Actividad Física y del Deporte");

        Grade grade31 = new Grade(
                "Grado en Comunicación Audiovisual");

        Grade grade32 = new Grade(
                "Grado en Conservación y Restauración de Bienes Culturales");

        Grade grade33 = new Grade(
                "Grado en Criminología");

        Grade grade34 = new Grade(
                "Grado en Derecho");

        Grade grade35 = new Grade(
                "Grado en Economía");

        Grade grade36 = new Grade(
                "Grado en Edificación");

        Grade grade37 = new Grade(
                "Grado en Educación Infantil");

        Grade grade38 = new Grade(
                "Grado en Educación Primaria");

        Grade grade39 = new Grade(
                "Grado en Enfermería");

        Grade grade40 = new Grade(
                "Grado en Estadística");

        Grade grade41 = new Grade(
                "Grado en Estudios Árabes e Islámicos");

        Grade grade42 = new Grade(
                "Grado en Estudios de Asia Oriental");

        Grade grade43 = new Grade(
                "Grado en Estudios Franceses");

        Grade grade44 = new Grade(
                "Grado en Estudios Ingleses");

        Grade grade45 = new Grade(
                "Grado en Farmacia");

        Grade grade46 = new Grade(
                "Grado en Filología Clásica");

        Grade grade47 = new Grade(
                "Grado en Filología Hispánica");

        Grade grade48 = new Grade(
                "Grado en Filosofía");

        Grade grade49 = new Grade(
                "Grado en Finanzas y Contabilidad");

        Grade grade50 = new Grade(
                "Grado en Física");

        Grade grade51 = new Grade(
                "Grado en Fisioterapia");

        Grade grade52 = new Grade(
                "Grado en Fundamentos de Arquitectura");

        Grade grade53 = new Grade(
                "Grado en Geografía y Gestión del Territorio");

        Grade grade54 = new Grade(
                "Grado en Gestión y Administración Pública");

        Grade grade55 = new Grade(
                "Grado en Historia");

        Grade grade56 = new Grade(
                "Grado en Historia del Arte");

        Grade grade57 = new Grade(
                "Grado en Ingeniería Aeroespacial");

        Grade grade58 = new Grade(
                "Grado en Ingeniería Agrícola");

        Grade grade59 = new Grade(
                "Grado en Ingeniería Civil");

        Grade grade60 = new Grade(
                "Grado en Ingeniería de la Energía");

        Grade grade61 = new Grade(
                "Grado en Ingeniería de la Salud");

        Grade grade62 = new Grade(
                "Grado en Ingeniería de las Tecnologías de Telecomuncicación");

        Grade grade63 = new Grade(
                "Grado en Ingeniería de Materiales");

        Grade grade64 = new Grade(
                "Grado en Ingeniería de Organización Industrial");

        Grade grade65 = new Grade(
                "Grado en Ingeniería de Tecnologías Industriales");

        Grade grade66 = new Grade(
                "Grado en Ingeniería Eléctrica");

        Grade grade67 = new Grade(
                "Grado en Ingeniería Electrónica Industrial");

        Grade grade68 = new Grade(
                "Grado en Ingeniería Electrónica, Robótica y Mecatrónica");

        Grade grade69 = new Grade(
                "Grado en Ingeniería en Diseño Industrial y Desarrollo del Producto");

        Grade grade70 = new Grade(
                "Grado en Ingeniería Informática-Ingeniería de Computadores");

        Grade grade71 = new Grade(
                "Grado en Ingeniería Informática-Ingeniería del Software");

        Grade grade72 = new Grade(
                "Grado en Ingeniería Informática-Tecnologías Informáticas");

        Grade grade73 = new Grade(
                "Grado en Ingeniería Mecánica");

        Grade grade74 = new Grade(
                "Grado en Ingeniería Química");

        Grade grade75 = new Grade(
                "Grado en Ingeniería Química Industrial");

        Grade grade76 = new Grade(
                "Grado en Lengua y Literatura Alemanas");

        Grade grade77 = new Grade(
                "Grado en Marketing e Investigación de Mercados");

        Grade grade78 = new Grade(
                "Grado en Matemáticas");

        Grade grade79 = new Grade(
                "Grado en Medicina");

        Grade grade80 = new Grade(
                "Grado en Odontología");

        Grade grade81 = new Grade(
                "Grado en Óptica y Optometría");

        Grade grade82 = new Grade(
                "Grado en Pedagogía");

        Grade grade83 = new Grade(
                "Grado en Periodismo");

        Grade grade84 = new Grade(
                "Grado en Podología");

        Grade grade85 = new Grade(
                "Grado en Psicología");

        Grade grade86 = new Grade(
                "Grado en Publicidad y Relaciones Públicas");

        Grade grade87 = new Grade(
                "Grado en Química");

        Grade grade88 = new Grade(
                "Grado en Relaciones Laborales y Recursos Humanos");

        Grade grade89 = new Grade(
                "Grado en Turismo");

        List<Grade> grades = Arrays.asList(grade1, grade2, grade3, grade4, grade5, grade6, grade7, grade8, grade9, grade10, grade11, grade12, grade13, grade14, grade15, grade16, grade17, grade18, grade19, grade20, grade21, grade22,
                grade23, grade24, grade25, grade26, grade27, grade28, grade29, grade30, grade31, grade32, grade33, grade34, grade35, grade36, grade27, grade38, grade39, grade40, grade41, grade42, grade43, grade44, grade45, grade46, grade47, grade48, grade49,
                grade50, grade51, grade52, grade53, grade54, grade55, grade56, grade57, grade58, grade59, grade60, grade61, grade62, grade63, grade64, grade65, grade66, grade67, grade68, grade69, grade70, grade71, grade72, grade73, grade74, grade75, grade76,
                grade77, grade78, grade79, grade80, grade81, grade82, grade83, grade84, grade85, grade86, grade87, grade88, grade89);
        gradeRepository.saveAll(grades);

        //Departments -----------------------------------

        Department department1 = new Department(
                "Escuela Politécnica Superior",
                Arrays.asList(grade13, grade15, grade16, grade66, grade67, grade69, grade73, grade75)
        );

        Department department2 = new Department(
                "Escuela Técnica Superior de Arquitectura",
                Arrays.asList(grade25, grade52)
        );

        Department department3 = new Department(
                "Escuela Técnica Superior de Ingeniería",
                Arrays.asList(grade57, grade59, grade60, grade62, grade64, grade65, grade68, grade74)
        );

        Department department4 = new Department(
                "Escuela Técnica Superior de Ingeniería Agronómica",
                Arrays.asList(grade14, grade58)
        );

        Department department5 = new Department(
                "Escueña Técnica Superior de Ingeniería de Edificación",
                Arrays.asList(grade36)
        );

        Department department6 = new Department(
                "Escuela Técnica Superior de Ingeniería Informática",
                Arrays.asList(grade17, grade61, grade70, grade71, grade72)
        );

        Department department7 = new Department(
                "Facultad de Bellas Artes",
                Arrays.asList(grade26, grade32)
        );

        Department department8 = new Department(
                "Facultad de Ciencias de la Educación",
                Arrays.asList(grade5, grade11, grade18, grade30, grade37, grade38, grade82)
        );

        Department department9 = new Department(
                "Facultad de Ciencias del Trabajo",
                Arrays.asList(grade8, grade88)
        );

        Department department10 = new Department(
                "Facultad de Derecho",
                Arrays.asList(grade1, grade2, grade3, grade4, grade33, grade34, grade54)
        );

        Department department11 = new Department(
                "Facultas de Filología",
                Arrays.asList(grade5, grade7, grade18, grade41, grade43, grade44, grade46, grade47, grade76)
        );

        Department department12 = new Department(
                "Facultad de Filosofía",
                Arrays.asList(grade42, grade48)
        );

        Department department13 = new Department(
                "Facultad de Geografía e Historia",
                Arrays.asList(grade12, grade23, grade24, grade53, grade55, grade56)
        );

        Department department14 = new Department(
                "Facultad de Psicología",
                Arrays.asList(grade85)
        );

        Department department15 = new Department(
                "Facultad de Biología",
                Arrays.asList(grade27, grade29)
        );

        Department department16 = new Department(
                "Facultad de Enfermería, Fisioterapia y Podología",
                Arrays.asList(grade11, grade39, grade84)
        );

        Department department17 = new Department(
                "Facultad de Farmacia",
                Arrays.asList(grade6, grade45, grade81)
        );

        Department department18 = new Department(
                "Facultad de Física",
                Arrays.asList(grade9, grade10, grade21, grade50, grade63)
        );

        Department department19 = new Department(
                "Facultad de Matemáticas",
                Arrays.asList(grade10, grade17, grade19, grade40, grade78)
        );

        Department department20 = new Department(
                "Facultad de Medicina",
                Arrays.asList(grade28, grade79)
        );

        Department department21 = new Department(
                "Facultad de Odontología",
                Arrays.asList(grade80)
        );

        Department department22 = new Department(
                "Facultad de Química",
                Arrays.asList(grade21, grade87)
        );

        Department department23 = new Department(
                "Facultad de Ciencias Económicas y Empresariales",
                Arrays.asList(grade2, grade22, grade35, grade51, grade77)
        );

        Department department24 = new Department(
                "Facultad de Comunicación",
                Arrays.asList(grade20, grade31, grade83, grade86)
        );

        Department department25 = new Department(
                "Facultad de Turismo y Finanzas",
                Arrays.asList(grade3, grade8, grade49, grade89)
        );

        List<Department> departments = Arrays.asList(department1, department2, department3, department4, department5, department6, department7, department8, department9,
                department10, department11, department12, department13, department14, department15, department16, department17, department18, department19, department20,
                department21, department22, department23, department24, department25);
        departmentRepository.saveAll(departments);

        //Areas -----------------------------------------

        Area area1 = new Area(
                "Tecnología",
                Arrays.asList(department1, department2, department3, department4, department5, department6)
        );

        Area area2 = new Area(
                "Humanidades",
                Arrays.asList(department7, department8, department9, department10, department11, department12, department13, department14)
        );

        Area area3 = new Area(
                "Bio-Ciencias",
                Arrays.asList(department15, department16, department17, department18, department19, department20, department21)
        );

        Area area4 = new Area(
                "Transversales",
                Arrays.asList(department23, department24, department25)
        );

        List<Area> areas = Arrays.asList(area1, area2, area3, area4);
        areaRepository.saveAll(areas);

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
                Arrays.asList(projectMonitoring1, projectMonitoring2)
                ,egr1);

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
                Arrays.asList(projectMonitoring3, projectMonitoring4)
                ,pro1);

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
                Arrays.asList(projectMonitoring5)
                ,est1);

        Project a = projectRepository.save(project1);
        Project b = projectRepository.save(project2);
        Project c = projectRepository.save(project3);

        Set<Project> pa = aa.getProjects();
        pa.add(a);
        aa.setProjects(pa);
        userRepository.save(aa);

        Set<Project> pb = bb.getProjects();
        pb.add(b);
        bb.setProjects(pb);
        userRepository.save(bb);

        Set<Project> pc = cc.getProjects();
        pc.add(c);
        cc.setProjects(pc);
        userRepository.save(cc);



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