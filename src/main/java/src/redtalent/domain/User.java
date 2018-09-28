package src.redtalent.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.UserAccount;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Users")
public class User extends Actor{

    //Attributes -----------------------------------------------
    private String role;

    //Constructors ---------------------------------------------
    public User(){
        super();
    }

    public User(String email, String name,String surname, boolean isSuspicious, UserAccount userAccount, String role,AcademicProfile academicProfile,
                List<Application> applications, List<Evaluation> evaluations, List<Comment> comments) {
        super(email, name,surname, isSuspicious, userAccount);
        this.role = role;
        this.academicProfile = academicProfile;
        this.applications = applications;
        this.evaluations = evaluations;
        this.comments = comments;
    }

   //Getters and setters -----------------------------------------
    @NotBlank
    @Pattern(regexp = "^STUDENT|GRADUATE|PROFESSOR$")
    public String getRole(){
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //Relationships ---------------------------------------------
    private AcademicProfile academicProfile;
    private List<Application> applications;
    private List<Evaluation> evaluations;
    private List<Comment> comments;

    @Valid
    public AcademicProfile getAcademicProfile() {
        return academicProfile;
    }

    public void setAcademicProfile(AcademicProfile academicProfile) {
        this.academicProfile = academicProfile;
    }

    @Valid
    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    @Valid
    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    @Valid
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
