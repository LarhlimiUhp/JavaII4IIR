package com.examen.stock.model;

public class Produit {
    private String nom;
    private double prix;

    public Produit(String nom, double prix) {
        this.nom = nom;
        this.prix = prix;
    }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    @Override
    public String toString() { return nom + " (" + prix + "€)"; }
}
