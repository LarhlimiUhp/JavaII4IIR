Voici la **Partie 1** de votre support de cours, conçue pour vous préparer aussi bien aux questions théoriques (QCM) qu'aux exercices de complétion de code demandés à l'examen.

---

# Partie 1 : Programmation Java Avancée
**Objectifs :** Maîtriser la robustesse (Exceptions), la flexibilité (Généricité) et l'efficacité (Collections).

---

## 1. Gestion Avancée des Exceptions
L'examen insiste sur la capacité à identifier et corriger des erreurs. Une bonne gestion des exceptions est cruciale pour la stabilité d'une application (notamment en JDBC).

### A. Le Try-with-Resources
Apparu en Java 7, il permet de fermer automatiquement les ressources (fichiers, connexions base de données) qui implémentent `AutoCloseable`.
*   **Pourquoi ?** Évite les fuites de mémoire sans avoir besoin d'un bloc `finally` lourd.

```java
// Exemple : Lecture d'une ressource
try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) {
    System.err.println("Erreur de lecture : " + e.getMessage());
}
// Le BufferedReader est fermé automatiquement ici, même si une exception survient.
```

### B. Création d'Exceptions Personnalisées
Pour une couche DAO propre, on crée souvent ses propres exceptions pour masquer les détails techniques (ex: masquer une `SQLException` derrière une `DataAccessException`).

```java
public class SoldeInsuffisantException extends Exception {
    public SoldeInsuffisantException(double montant) {
        super("Il manque " + montant + " € pour effectuer la transaction.");
    }
}
```

---

## 2. La Généricité
La généricité permet de créer des classes ou méthodes qui fonctionnent avec différents types tout en garantissant la **sécurité de typage** à la compilation.

### A. Classe Générique
```java
public class Boite<T> { // T est un paramètre de type
    private T contenu;

    public void ajouter(T contenu) { this.contenu = contenu; }
    public T recuperer() { return contenu; }
}
```

### B. Les Wildcards (?) et Bornes (Bounds)
C'est un point classique des QCM :
*   `<? extends T>` : Accepte T ou n'importe quelle sous-classe de T (**Lecture seule**).
*   `<? super T>` : Accepte T ou n'importe quelle classe parente de T (**Écriture possible**).

---

## 3. Utilisation Avancée des Collections & Streams
L'examen porte sur le choix de la bonne collection et la manipulation des données.

*   **List (ArrayList)** : Accès rapide par index, doublons autorisés.
*   **Set (HashSet)** : Pas de doublons, pas d'ordre garanti.
*   **Map (HashMap)** : Stockage clé-valeur.

### L'API Stream (Complétion de code)
Très utile pour filtrer et transformer des collections de manière concise :
```java
List<String> noms = Arrays.asList("Jean", "Alice", "Bob", "Anna");

List<String> resultat = noms.stream()
    .filter(n -> n.startsWith("A")) // Filtre ceux qui commencent par A
    .map(String::toUpperCase)       // Transforme en majuscules
    .sorted()                       // Trie par ordre alphabétique
    .collect(Collectors.toList());  // Convertit en liste
```

---

## 4. Projet Global : Système de Gestion de Stock Générique

Ce projet combine les trois concepts : une **exception personnalisée**, une interface **générique**, et l'utilisation de **Collections/Streams**.

### Structure du projet :
1.  `Produit` : La classe de base.
2.  `StockException` : L'exception métier.
3.  `Repository<T>` : L'interface générique (préparation à la structure DAO).
4.  `GestionnaireStock<T>` : La classe logique utilisant les Collections.

#### Code du Projet :

```java
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
```

### Pourquoi ce projet est-il complet pour la Partie 1 ?
1.  **Généricité :** `GestionnaireStock<T extends Produit>` montre comment restreindre un type générique à une classe parente.
2.  **Exceptions :** Le `try-catch` dans le `main` et le `orElseThrow` dans le Stream montrent la gestion propre des erreurs.
3.  **Collections :** L'utilisation de `ArrayList` et des `Streams` (`filter`, `findFirst`, `collect`) couvre la manipulation avancée de données demandée.

**Souhaitez-vous passer à la Partie 2 (Threads et Concurrence) ?**
