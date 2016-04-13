package com.app.dlc.dlc.model;

/**
 * Created by Guillaume on 08/04/2016.
 */
public class Distributeur {
    private int id;
    private String nom;
    private String addresse;
    private String horaireOuverture;
    private String horaireFermerture;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getHoraireFermerture() {
        return horaireFermerture;
    }

    public void setHoraireFermerture(String horaireFermerture) {
        this.horaireFermerture = horaireFermerture;
    }

    public String getHoraireOuverture() {
        return horaireOuverture;
    }

    public void setHoraireOuverture(String horaireOuverture) {
        this.horaireOuverture = horaireOuverture;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
