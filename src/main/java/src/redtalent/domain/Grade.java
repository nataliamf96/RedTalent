package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "Grades")
public class Grade extends DomainEntity{

    //Attributes -----------------------------------------------
    private String name;

    //Constructors -----------------------------------------------
    public Grade(){
        super();
    }

    public Grade(String name){
        this.name = name;
    }

    //Getters and setters -----------------------------------------------

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
