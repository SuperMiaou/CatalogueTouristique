package devmat.cataloguetouristique;

import java.io.Serializable;



/**
 * Created by rcdsm on 18/05/15.
 */
public class Etablissement implements Serializable {


    private int id;

    private String name;
    private String price;
    private String number;
    private String picture;
    private String adress;

    private String content;
    private String ouverture;

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOuverture() {
        return ouverture;
    }

    public void setOuverture(String ouverture) {
        this.ouverture = ouverture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Etablissement(int id, String name, String price, String number, String adress, String content, String ouverture, String picture) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.number = number;
        this.adress = adress;
        this.content = content;
        this.ouverture = ouverture;
        this.picture = picture;

    }
}
