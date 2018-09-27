package src.redtalent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import src.redtalent.repositories.*;
import src.redtalent.security.Authority;
import java.util.*;


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
    }
}



