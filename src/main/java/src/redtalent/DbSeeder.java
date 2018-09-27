package src.redtalent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.domain.Evaluation;
import src.redtalent.domain.ProjectMonitoring;
import src.redtalent.repositories.*;
import src.redtalent.security.Authority;
import java.util.*;


@Component
public class DbSeeder implements CommandLineRunner{

    //Repositories -------------------------------------------------
    private AcademicProfileRepository academicProfileRepository;
    private AdministratorRepository administratorRepository;
    private AlertRepository alertRepository;
    private ApplicationRepository applicationRepository;
    private CommentRepository commentRepository;
    private CompanyRepository companyRepository;
    private EvaluationRepository evaluationRepository;
    private GradeRepository gradeRepository;
    private PhaseRepository phaseRepository;
    private ProjectMonitoringRepository projectMonitoringRepository;
    private ProjectRepository projectRepository;
    private TagRepository tagRepository;
    private TeamRepository teamRepository;
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

        //Borro los documentos de la BD
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

        //Creo las Authorities ADMIN, USER y COMPANY
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
        AcademicProfile academicProfile1 = new AcademicProfile();
    }
}



