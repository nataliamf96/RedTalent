package src.redtalent.domain;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "Administradores")
public class Administrator extends DomainEntity{

    //Attributes -----------------------------------------------
    @NotBlank
    private String fullname;
    private String image;
    private Account account;

    //Constructors -----------------------------------------------
    public Administrator(){
        super();
        this.grades = new ArrayList<>();
    }

    public Administrator(Account account,String fullname,String image){
        this.fullname = fullname;
        this.account = account;
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Relationships -----------------------------------------------
    private List<Grade> grades;

    @NotNull
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
