package src.redtalent.domain;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document(collection = "Curriculum")
public class Curriculum extends DomainEntity{

    private String urlLinkedin;

    public Curriculum(){
        super();
    }

    public Curriculum(String urlLinkedin){
        this.urlLinkedin = urlLinkedin;
    }

    @URL
    @NotBlank
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    public void setUrlLinkedin(String urlLinkedin) {
        this.urlLinkedin = urlLinkedin;
    }

}
