package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Document(collection = "Applications")
public class Application extends DomainEntity{

    // Attributes -----------------------------------------------
    private Date moment;
    private String status;

    // Constructors -----------------------------------------------
    public Application(){
        super();
    }

    public Application(Date moment, String status){
        this.moment = moment;
        this.status = status;
    }

    // Getters and setters -----------------------------------------------
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return this.moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    @NotBlank
    @Pattern(regexp = "^ACEPTADO|PENDIENTE|DENEGADO")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
