package com.app.dlc.dlc.model;

/**
 * Created by Guillaume on 03/04/2016.
 */
public class Produit {
    private String nom;
    private double prixFinal;
    private double prixInitial;
    private String dlc;
    private String quantite;
    private String categorie;
    private String image;
    private String idDistributeur;
    public String id;



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrixFinal() {
        return prixFinal;
    }

    public void setPrixFinal(double prixFinal) {
        this.prixFinal = prixFinal;
    }

    public String getDlc() {
        return dlc;
    }

    public void setDlc(String dlc) {
        this.dlc = dlc;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public double getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(double prixInitial) {
        this.prixInitial = prixInitial;
    }
    public String getIdDistributeur() {
        return idDistributeur;
    }

    public void setIdDistributeur(String idDistributeur) {
        this.idDistributeur = idDistributeur;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
