package src.redtalent.forms;

import org.hibernate.validator.constraints.URL;
import src.redtalent.domain.Grade;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CurriculumForm {

    private String urlLinkedin;
    private String grade;
    private String userType;
    private String description;

    public CurriculumForm(){
        super();
    }

    @URL
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    public void setUrlLinkedin(String urlLinkedin) {
        this.urlLinkedin = urlLinkedin;
    }

    @NotNull
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @NotBlank
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
