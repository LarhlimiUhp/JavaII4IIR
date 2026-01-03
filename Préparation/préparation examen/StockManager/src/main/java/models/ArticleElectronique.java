package models;

public class ArticleElectronique extends Produit {
    private int garantieMois;

    public ArticleElectronique(String id, String nom, double prix, int garantieMois) {
        super(id, nom, prix);
        this.garantieMois = garantieMois;
    }

    public int getGarantieMois() { return garantieMois; }

    @Override
    public String toString() {
        return super.toString() + " [Garantie: " + garantieMois + " mois]";
    }
}
