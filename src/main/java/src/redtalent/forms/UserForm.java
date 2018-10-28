package src.redtalent.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.File;

public class UserForm {

    private String email;
    private String password;
    private String fullname;
    private String role;
    private Boolean terms;
    private String image;

    public UserForm(){
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NotNull
    public Boolean getTerms() {
        return terms;
    }

    public void setTerms(Boolean terms) {
        this.terms = terms;
    }

    @NotBlank
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @NotBlank
    @Pattern(regexp = "^ESTUDIANTE|PROFESOR|EGRESADO$")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}
