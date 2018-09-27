package src.redtalent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import src.redtalent.domain.*;
import src.redtalent.repositories.*;
import src.redtalent.security.Authority;
import src.redtalent.security.UserAccount;
import src.redtalent.security.UserAccountRepository;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class DbSeeder implements CommandLineRunner{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AcademicProfileRepository academicProfileRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public DbSeeder(ProjectRepository projectRepository,TeamRepository teamRepository,ApplicationRepository applicationRepository,AcademicProfileRepository academicProfileRepository,GradeRepository gradeRepository,AdministratorRepository administratorRepository,CompanyRepository companyRepository,UserRepository userRepository){
        this.administratorRepository = administratorRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
        this.academicProfileRepository = academicProfileRepository;
        this.applicationRepository = applicationRepository;
        this.teamRepository = teamRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        //Codificación para contraseñas
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        //Borro los documentos de la BD
        userRepository.deleteAll();
        companyRepository.deleteAll();
        administratorRepository.deleteAll();
        gradeRepository.deleteAll();
        academicProfileRepository.deleteAll();
        applicationRepository.deleteAll();
        teamRepository.deleteAll();
        projectRepository.deleteAll();

        //Creo las Authorities de USER, COMPANY y ADMIN
        Collection<Authority> adminAuthority = new HashSet<Authority>();
        Authority authority = new Authority();
        authority.setAuthority(Authority.ADMIN);
        adminAuthority.add(authority);

        Collection<Authority> userAuthority = new HashSet<Authority>();
        Authority authority1 = new Authority();
        authority1.setAuthority(Authority.USER);
        userAuthority.add(authority1);

        Collection<Authority> companyAuthority = new HashSet<Authority>();
        Authority authority2 = new Authority();
        authority2.setAuthority(Authority.COMPANY);
        companyAuthority.add(authority2);

        //Creación de Academic Profiles
        AcademicProfile ap1 = new AcademicProfile("Bachillerato","Ciencias Sociales");
        AcademicProfile ap2 = new AcademicProfile("Formación Profesional","Técnico Informático");

        List<AcademicProfile> academicProfiles = Arrays.asList(ap1, ap2);
        academicProfileRepository.save(academicProfiles);

        //Creación de Applications
        Date moment = new Date();
        Date dateFuture = new Date(moment.getTime() + TimeUnit.DAYS.toMillis( 20 ));
        Date dateFuture2 = new Date(moment.getTime() + TimeUnit.DAYS.toMillis( 100 ));
        Application a1 = new Application(moment,"PENDING");
        Application a2 = new Application(moment,"PENDING");

        List<Application> applications = Arrays.asList(a1,a2);
        applicationRepository.save(applications);


        //Creación de Grades
        Grade grade1 = new Grade("Ingeniería del Software", "Universidad de Sevilla");
        Grade grade2 = new Grade("Ingeniería de Computadores", "Universidad de Sevilla");
        Grade grade3 = new Grade("Doble Grado en Administración y Dirección de Empresas y en Derecho", "Universidad de Sevilla");
        Grade grade4 = new Grade("Doble Grado en Geografía y Gestión del Territorio e Historia", "Universidad de Sevilla");

        List<Grade> grades = Arrays.asList(grade1,grade2,grade3,grade4);
        gradeRepository.save(grades);

        //Creación de ADMIN
        Administrator admin = new Administrator("admin@admin.com","Natalia","Morato Fernández",false,new UserAccount("admin",encoder.encodePassword("admin",null),adminAuthority,false),grades);

        //Creación de COMPANIES
        Company company1 = new Company("everis@everis.es","Everis","Center",false,new UserAccount("company1",encoder.encodePassword("company1",null), companyAuthority,false));
        Company company2 = new Company("everisSolutions@everis.es","Everis","Solutions",false,new UserAccount("company2",encoder.encodePassword("company2",null), companyAuthority,false));
        Company company3 = new Company("ayesa@ayesa.es","Ayesa","Ayesa",false,new UserAccount("company3",encoder.encodePassword("company3",null), companyAuthority,false));

        List<Administrator> administrators = Arrays.asList(admin);
        administratorRepository.save(administrators);

        List<Company> companies = Arrays.asList(company1,company2,company3);
        companyRepository.save(companies);

    }
}



