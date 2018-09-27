package src.redtalent.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import src.redtalent.security.UserAccount;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public abstract class Actor extends DomainEntity{

    //Attributes -----------------------------------------

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    private String name;
    private String surname;
    private boolean isSuspicious;

    //Constructors -----------------------------------------
    public Actor(){
        super();
    }

    public Actor(String email, String name, String surname, boolean isSuspicious, UserAccount userAccount) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.isSuspicious = isSuspicious;
        this.userAccount = userAccount;
    }

    //Getters and setters -----------------------------------------
    @Email
    @NotBlank
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @NotNull
    public boolean isSuspicious() {
        return isSuspicious;
    }

    public void setSuspicious(boolean suspicious) {
        isSuspicious = suspicious;
    }

    //Relationships -----------------------------------------
    private UserAccount userAccount;

    @Valid
    @NotNull
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

}
