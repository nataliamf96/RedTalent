package src.redtalent.forms;

import src.redtalent.domain.Grade;

import javax.validation.constraints.NotBlank;

public class UpdateUserForm {

    private String fullname;
    private String image;
    private String etiquetas;
    private Grade grade;

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

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }


}

