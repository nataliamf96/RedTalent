package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "Recomendations")
public class Recomendation extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String text;
    private Date moment;

    // Constructors ---------------------------------------------------
    public Recomendation(){
        super();
    }

    public Recomendation( String text, Date moment){
        this.text = text;
        this.moment = moment;
    }

    // Getters and setters ---------------------------------------------------

    @NotBlank
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

}
