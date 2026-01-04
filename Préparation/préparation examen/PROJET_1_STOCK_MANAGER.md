# Programmation Avancée : "Gestionnaire de Stock Générique"
Objectif : Créer une application capable de gérer n'importe quel type de produit (électronique, alimentaire, etc.) en utilisant la généricité et les collections avancées.

## Concepts Clés :
### Généricité : Créer une classe Stock<T extends Produit> avec des méthodes ajouter(T item), rechercher(String id).
Exceptions : Définir des exceptions personnalisées : ProduitEpuiseException, IdInvalideException.
Collections : Utiliser une HashMap pour un accès rapide aux produits par ID et des Streams pour filtrer les produits par prix ou catégorie.
Challenge : Implémenter un comparateur (Comparator) pour trier dynamiquement les produits selon différents critères.



## Projet Java : StockManager (Généricité, Collections, Exceptions)

### 1. Structure du Projet
```text
StockManager/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── exceptions/
│   │       │   └── IdInvalideException.java
│   │       ├── models/
│   │       │   ├── Produit.java
│   │       │   └── ArticleElectronique.java
│   │       ├── services/
│   │       │   └── Stock.java
│   │       └── Main.java
└── pom.xml
```

---

## 2. Code Source

### `IdInvalideException.java`
```java
package exceptions;

public class IdInvalideException extends Exception {
    public IdInvalideException(String message) { super(message); }
}
```

### `Produit.java` (Classe Abstraite)
```java
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
```

### `Stock.java` (Classe Générique)
```java
package services;

import models.Produit;
import exceptions.IdInvalideException;
import java.util.*;

public class Stock<T extends Produit> {
    private Map<String, T> produits = new HashMap<>();

    public void ajouter(T produit) throws IdInvalideException {
        if (produit.getId() == null || produit.getId().trim().isEmpty()) {
            throw new IdInvalideException("L'ID ne peut pas être vide.");
        }
        if (produits.containsKey(produit.getId())) {
            throw new IdInvalideException("ID " + produit.getId() + " déjà existant.");
        }
        produits.put(produit.getId(), produit);
    }

    public T rechercher(String id) { return produits.get(id); }

    public List<T> listerTout() { return new ArrayList<>(produits.values()); }
}
```

---

## 3. Compte Rendu et Points Clés

### La Généricité (`T`)
L'utilisation de **`T`** est un paramètre de type. 
*   **Placeholder** : Il représente le type réel (ex: `ArticleElectronique`) au moment de l'instanciation.
*   **`extends Produit`** : C'est une borne supérieure. Elle garantit que `T` possède les méthodes de `Produit`.
*   **Avantages** : Suppression des casts manuels, sécurité de type à la compilation, et code réutilisable.

### Points à retenir:
1.  **Collections** : La `HashMap` est idéale pour l'accès par clé unique ($O(1)$).
2.  **Exceptions** : Les exceptions héritant de `Exception` sont "checkées" et imposent un bloc `try-catch`.
3.  **Encapsulation** : Toujours utiliser des attributs `private` et des getters/setters `public`.


