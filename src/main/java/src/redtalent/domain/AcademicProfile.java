package src.redtalent.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "AcademicProfiles")
public class AcademicProfile extends DomainEntity{

    //Attributes ---------------------------------------------------

    private String name;
    private String description;

    //Constructors ---------------------------------------------------

    public AcademicProfile(){
        super();
    }

    public AcademicProfile(String name, String description){
        this.name = name;
        this.description = description;
    }

    //Getters and setters ---------------------------------------------------

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
