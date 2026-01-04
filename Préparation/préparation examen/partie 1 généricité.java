import java.util.*;
import java.util.stream.Collectors;

// 1. Exception Personnalisée
class StockException extends Exception {
    public StockException(String message) { super(message); }
}

// 2. Modèle de donnée
class Produit {
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

// 3. Interface Générique (Style DAO)
interface Repository<T> {
    void ajouter(T element);
    List<T> listerTout();
    T trouverParNom(String nom) throws StockException;
}

// 4. Implémentation avec Collections et Généricité
class GestionnaireStock<T extends Produit> implements Repository<T> {
    private List<T> inventaire = new ArrayList<>();

    @Override
    public void ajouter(T element) {
        inventaire.add(element);
        System.out.println("Ajout de : " + element.getNom());
    }

    @Override
    public List<T> listerTout() {
        return new ArrayList<>(inventaire);
    }

    @Override
    public T trouverParNom(String nom) throws StockException {
        return inventaire.stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new StockException("Produit '" + nom + "' introuvable !"));
    }

    // Utilisation des Streams pour une analyse technique
    public List<T> filtrerParPrixMin(double seuil) {
        return inventaire.stream()
                .filter(p -> p.getPrix() >= seuil)
                .collect(Collectors.toList());
    }
}

// 5. Classe Principale
public class MainApp {
    public static void main(String[] args) {
        GestionnaireStock<Produit> monStock = new GestionnaireStock<>();

        // Ajout de données
        monStock.ajouter(new Produit("Ordinateur", 1200.0));
        monStock.ajouter(new Produit("Souris", 25.0));
        monStock.ajouter(new Produit("Clavier", 45.0));

        // Test de recherche avec gestion d'exception
        try {
            System.out.println("\nRecherche d'un produit...");
            Produit p = monStock.trouverParNom("Ecran"); // Va générer une exception
        } catch (StockException e) {
            System.err.println("Erreur : " + e.getMessage());
        }

        // Test des Streams
        System.out.println("\nProduits de plus de 40€ :");
        monStock.filtrerParPrixMin(40).forEach(System.out::println);
    }
}