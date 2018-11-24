package src.redtalent.domain;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document(collection = "Curriculum")
public class Curriculum extends DomainEntity{

    private String urlLinkedin;
    private Grade grade;

    public Curriculum(){
        super();
    }

    public Curriculum(String urlLinkedin, Grade grade){
        this.urlLinkedin = urlLinkedin;
        this.grade = grade;
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
    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
