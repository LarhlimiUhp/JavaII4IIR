import models.*;
import services.Stock;
import exceptions.IdInvalideException;

public class Main {
    public static void main(String[] args) {
        Stock<Produit> monStock = new Stock<>();

        try {
            System.out.println("--- Test d'ajout de produits ---");
            monStock.ajouter(new ArticleElectronique("E001", "Smartphone", 799.99, 24));
            monStock.ajouter(new ArticleElectronique("E002", "Ordinateur", 1200.00, 12));

            System.out.println("Liste complète du stock :");
            for (Produit p : monStock.listerTout()) {
                System.out.println("- " + p);
            }

            System.out.println("\n--- Test recherche ---");
            Produit p = monStock.rechercher("E001");
            System.out.println("Trouvé : " + p);

            System.out.println("\n--- Test exception (ID vide) ---");
            monStock.ajouter(new ArticleElectronique("", "Erreur", 0, 0));

        } catch (IdInvalideException e) {
            System.err.println("ERREUR CAPTURÉE : " + e.getMessage());
        }

        try {
            System.out.println("\n--- Test exception (ID en double) ---");
            monStock.ajouter(new ArticleElectronique("E001", "Smartphone Bis", 500, 6));
        } catch (IdInvalideException e) {
            System.err.println("ERREUR CAPTURÉE : " + e.getMessage());
        }
    }
}
