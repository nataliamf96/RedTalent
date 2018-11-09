package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Relationships ---------------------------------------------------
}
