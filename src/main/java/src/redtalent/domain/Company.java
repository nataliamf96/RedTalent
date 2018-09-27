package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.UserAccount;

@Document(collection = "Companies")
public class Company extends Actor{

    //Attributes -----------------------------------------------

    //Constructors -----------------------------------------------
    public Company(){
        super();
    }

    public Company(String email, String name,String surname, boolean isSuspicious, UserAccount userAccount) {
        super(email, name,surname, isSuspicious, userAccount);
    }

    //Getters and setters -----------------------------------------------

    //Relationships -----------------------------------------------

}
