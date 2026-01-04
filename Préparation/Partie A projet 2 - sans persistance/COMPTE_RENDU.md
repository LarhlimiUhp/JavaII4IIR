# Compte Rendu Global : Projet de Gestion de Stock

## 1. Introduction
Ce projet consiste en la mise en place d'une application Java modulaire pour la gestion d'un inventaire de produits. L'objectif était de structurer un code monolithique en un projet Maven organisé, appliquant les meilleures pratiques de programmation orientée objet et les fonctionnalités modernes de Java (Java 17+).

## 2. Architecture du Projet
Le projet suit une structure Maven standard, facilitant la gestion des dépendances et le cycle de vie du développement.

### Arborescence des fichiers
```text
Partie A projet 2/
├── pom.xml                           # Configuration Maven
└── src/main/java/com/examen/stock/
    ├── MainApp.java                  # Point d'entrée
    ├── exception/
    │   └── StockException.java       # Gestion des erreurs métier
    ├── model/
    │   └── Produit.java              # Modèle de données
    ├── repository/
    │   └── Repository.java           # Interface de persistance
    └── service/
        └── GestionnaireStock.java    # Logique métier et Streams
```

## 3. Analyse Détaillée des Composants

### A. Gestion des Erreurs (`StockException.java`)
Placée dans le package `exception`, cette classe personnalisée permet de distinguer les erreurs liées au stock des erreurs système générales. En tant que **Checked Exception**, elle garantit que les erreurs de recherche sont traitées explicitement dans le flux du programme.

### B. Modèle de Données (`Produit.java`)
Cette classe implémente l'**encapsulation**. Les données sont protégées par des accès privés, et la méthode `toString()` est redéfinie pour fournir une représentation textuelle claire des objets (ex: "Ordinateur (1200.0€)").

### C. Abstraction et Généricité (`Repository.java`)
L'interface `Repository<T>` définit un contrat générique. Cette approche permet de réutiliser la même structure de gestion de données pour d'autres types d'objets à l'avenir, rendant le système très flexible.

### D. Logique Métier et Streams (`GestionnaireStock.java`)
C'est le composant le plus technique du projet :
- **Bandes de type (`T extends Produit`)** : Assure que le gestionnaire ne manipule que des objets ayant les propriétés d'un produit.
- **Java Streams** : Utilisés pour le filtrage (`filter`) et la recherche, offrant une syntaxe déclarative fluide.
- **Optional** : Permet d'éviter les `NullPointerException` en gérant élégamment l'absence de résultat via `orElseThrow()`.

## 4. Guide d'Exécution

### Compilation
Pour compiler le projet et générer les fichiers `.class` :
```bash
mvn clean compile
```

### Exécution
Pour lancer l'application et voir les tests en action :
```bash
java -cp "target\classes" com.examen.stock.MainApp
```

## 5. Résultats Obtenus
L'exécution valide les points suivants :
1. **Ajout dynamique** de produits dans le stock.
2. **Gestion robuste des exceptions** : Une tentative de recherche d'un produit inexistant ("Ecran") déclenche une `StockException` capturée et affichée proprement.
3. **Analyse de données** : Le filtrage par prix minimal (ex: > 40€) via les Streams fonctionne correctement, retournant uniquement les articles concernés.

## 6. Note sur la Sécurité de Type
### Question : Le gestionnaire peut-il manipuler des objets n'ayant pas les propriétés d'un produit ?

**Réponse : Non.** Le code utilise une **borne générique** (`T extends Produit`) dans la classe `GestionnaireStock`. Ce mécanisme garantit deux choses :
- **Protection à la compilation** : Si vous essayez d'utiliser le gestionnaire avec une classe qui n'hérite pas de `Produit` (ex: `Client`), le compilateur Java générera une erreur immédiate.
- **Accès garanti aux méthodes** : Puisque Java sait que `T` est obligatoirement un `Produit`, il autorise l'utilisation sécurisée des méthodes `.getNom()` et `.getPrix()` à l'intérieur des Streams sans risque d'erreur de cast ou de méthode inexistante.

---
**Conclusion** : Ce projet démontre une maîtrise de la structuration Java, alliant principes SOLID, généricité et programmation fonctionnelle.
