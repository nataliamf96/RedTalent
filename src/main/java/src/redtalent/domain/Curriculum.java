package src.redtalent.domain;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document(collection = "Curriculum")
public class Curriculum extends DomainEntity{

    private String urlLinkedin;
    private Grade grade;
    private String description;
    private Boolean realized;

    public Curriculum(){
        super();
    }

    public Curriculum(String urlLinkedin, Grade grade, String description, Boolean realized){
        this.urlLinkedin = urlLinkedin;
        this.grade = grade;
        this.description = description;
        this.realized = realized;
    }

    @URL
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    public void setUrlLinkedin(String urlLinkedin) {
        this.urlLinkedin = urlLinkedin;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Boolean getRealized() {
        return realized;
    }

    public void setRealized(Boolean realized) {
        this.realized = realized;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
