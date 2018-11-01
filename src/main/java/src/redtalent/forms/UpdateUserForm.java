package src.redtalent.forms;

public class UpdateUserForm {

    private String fullname;
    private String image;

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

    @Override
    public String toString() {
        return "updateUserForm{" +
                "fullname='" + fullname + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

