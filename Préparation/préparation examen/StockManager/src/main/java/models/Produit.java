package models;

public abstract class Produit {
    private String id;
    private String nom;
    private double prix;

    public Produit(String id, String nom, double prix) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }

    public String getId() { return id; }
    public String getNom() { return nom; }
    public double getPrix() { return prix; }

    @Override
    public String toString() {
        return nom + " (ID: " + id + ") - " + prix + "€";
    }
}
