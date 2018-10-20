package src.redtalent.forms;

import javax.validation.constraints.Size;

public class EditPasswordForm {

    private String	password;
    private String	newPassword;
    private String	confNewPassword;


    @Size(min = 5, max = 32)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Size(min = 5, max = 32)
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Size(min = 5, max = 32)
    public String getConfNewPassword() {
        return confNewPassword;
    }

    public void setConfNewPassword(String confNewPassword) {
        this.confNewPassword = confNewPassword;
    }

    @Override
    public String toString() {
        return "EditActorPassword [password=" + password + ", newPassword=" + newPassword + ", confNewPassword=" + confNewPassword + ", getPassword()=" + getPassword() + ", getNewPassword()=" + getNewPassword() + ", getConfNewPassword()="
                + getConfNewPassword() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
