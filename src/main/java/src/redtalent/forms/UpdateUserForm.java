package src.redtalent.forms;

public class UpdateUserForm {

    private String fullname;
    private String image;
    private String etiquetas;

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

    @Override
    public String toString() {
        return "UpdateUserForm{" +
                "fullname='" + fullname + '\'' +
                ", image='" + image + '\'' +
                ", etiquetas='" + etiquetas + '\'' +
                '}';
    }
}

