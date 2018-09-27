package src.redtalent.domain;

public class Tag extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String name;

    // Constructors ---------------------------------------------------

    public Tag(){
        super();
    }

    public Tag(String name){
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
