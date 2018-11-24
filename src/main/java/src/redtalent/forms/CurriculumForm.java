package src.redtalent.forms;

import org.hibernate.validator.constraints.URL;
import src.redtalent.domain.Grade;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CurriculumForm {

    private String urlLinkedin;
    private String grade;

    public CurriculumForm(){
        super();
    }

    @URL
    @NotBlank
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
}
