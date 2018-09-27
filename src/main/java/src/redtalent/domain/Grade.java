package src.redtalent.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Grades")
public class Grade extends DomainEntity{

    //Attributes -----------------------------------------------
    private String name;
    private String university;

    //Constructors -----------------------------------------------
    public Grade(){
        super();
    }

    public Grade(String name, String university){
        this.name = name;
        this.university = university;
    }

    //Getters and setters -----------------------------------------------
    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
