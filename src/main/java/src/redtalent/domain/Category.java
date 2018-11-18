package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "Categories")
public class Category extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String name;

    // Constructors ---------------------------------------------------

    public Category(){
        super();
    }

    public Category(String name){
        this.name = name;
    }

    // Getters and setters ---------------------------------------------------

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Relationships ---------------------------------------------------
}
