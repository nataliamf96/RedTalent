package src.redtalent.forms;

import javax.validation.constraints.NotBlank;

public class UpdateAdminForm {

    private String fullname;
    private String image;

    public UpdateAdminForm(){
        super();
    }

    @NotBlank
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
}
