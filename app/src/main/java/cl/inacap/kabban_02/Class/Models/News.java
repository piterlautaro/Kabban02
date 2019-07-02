package cl.inacap.kabban_02.Class.Models;

public class News {
    private int type;
    private String owner;
    private String imageURL;
    private String publish;
    private String date;
    private String owner_name;

    public News() {
    }

    public News(int type, String owner, String imageURL, String publish, String date, String owner_name) {
        this.type = type;
        this.owner = owner;
        this.imageURL = imageURL;
        this.publish = publish;
        this.date = date;
        this.owner_name = owner_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
