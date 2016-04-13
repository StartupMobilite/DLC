package com.app.dlc.dlc.model;

/**
 * Created by Guillaume on 09/04/2016.
 */
public class Categorie {
    private String nom;
    private int id;


    public Categorie(int id) {
        this.id = id;
    }

    public Categorie(String nom, int id) {
        this.nom = nom;
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
