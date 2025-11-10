<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

# Supports de Cours : Streams en Java (4ème année IIR – EMSI)


***

## Introduction

Les **Streams en Java** constituent une évolution majeure introduite par Java 8, facilitant le traitement expressif, efficace et fonctionnel des collections. Ils permettent aussi bien des traitements séquentiels que parallèles, tout en favorisant un code concis, lisible et facilement maintenable.

**Objectifs** :

- Comprendre la motivation et les principes fondamentaux des Streams.
- Maîtriser les interfaces clés et les opérations principales (intermédiaires et terminales).
- Apprendre à manipuler les Streams sur des cas concrets liés à l'informatique et aux réseaux.
- Intégrer les bonnes pratiques pour des traitements performants et sûrs.

***

## I. Notion de Stream en Java

Un **Stream** est une séquence d’éléments provenant d’une source de données, permettant le traitement par pipeline (enchaînement d’opérations). Les Streams sont **immutables** (ne modifient pas la source) et reposent sur une exécution paresseuse (l’exécution réelle n’a lieu que lors de l’appel d’une opération terminale).

### Motivation

- Simplifier la manipulation et la transformation de collections.
- Favoriser un style de programmation fonctionnel.
- Faciliter le parallélisme et la lisibilité du code.

**Avant Streams** : itérations manuelles, code verbeux
**Avec Streams** : pipelines déclaratifs et traitements parallèles facilités

***

## II. Interfaces et classes essentielles

- `Stream<T>` : interface générique de flux d’objets.
- `IntStream`, `LongStream`, `DoubleStream` : variantes pour types primitifs, optimisant l’utilisation mémoire et les performances.
- `Collectors` (opérations terminales pour collecter les flux).

| Interface | Description |
| :-- | :-- |
| Stream<T> | Flux d’éléments de type générique |
| IntStream, LongStream | Flux optimisés pour types primitifs |
| BaseStream | Super-interface, gère la fermeture/parallel |
| Collectors | Outils pour agréger les résultats |


***

## III. Définition : Opérations intermédiaires et terminales

Dans l'API Streams de Java, les opérations sont classées en deux catégories : **opérations intermédiaires** et **opérations terminales**.

### Opérations intermédiaires

Ce sont des opérations qui transforment, filtrent ou manipulent les éléments du flux mais **ne produisent pas encore le résultat final**. Elles renvoient toujours un nouveau Stream, ce qui permet d'enchaîner plusieurs opérations sous forme de pipeline. Leur exécution est paresseuse : elles ne s'exécutent qu'au moment où une opération terminale est invoquée.

**Exemples :**

- `filter(Predicate)` : garde uniquement les éléments selon une condition.
- `map(Function)` : applique une transformation à chaque élément.
- `distinct()` : supprime les doublons.
- `sorted([Comparator])` : trie les éléments.
- `flatMap(Function)` : aplatit des structures imbriquées.


### Opérations terminales

Ce sont des opérations qui **déclenchent l'exécution réelle du pipeline** et produisent un résultat (qui n'est pas un Stream). Elles marquent la fin du pipeline.

**Exemples :**

- `forEach(Consumer)` : applique une action à chaque élément.
- `collect(Collector)` : rassemble les éléments dans une collection ou fait une agrégation.
- `reduce(BinaryOperator)` : combine les éléments pour obtenir une seule valeur.
- `count()` : compte le nombre d’éléments.
- `anyMatch(Predicate)`, `allMatch(Predicate)`, `findFirst()` : recherches ou tests sur flux.

**Résumé :**

- **Intermédiaires** : transforment ou filtrent, renvoient un Stream → construction du pipeline.
- **Terminales** : produisent un résultat final, déclenchent l'exécution → fin du pipeline.

***

## IV. Opérations : exemples et usage

### 1. Opérations intermédiaires

- `.filter(Predicate)` : filtre selon condition
- `.map(Function)` : transformation élément
- `.distinct()` : doublons supprimés
- `.sorted([Comparator])` : tri
- `.flatMap()` : aplatir


### 2. Opérations terminales

- `.forEach(Consumer)` : action sur chaque élément
- `.collect(Collector)` : rassemblement/agrégation
- `.reduce(BinaryOperator)` : réduction à valeur unique
- `.count()` : nombre
- `.anyMatch()`, `.allMatch()`, `.findFirst()` : recherche/condition


### 3. Séquentiel vs. Parallèle

- `.stream()` : pipeline séquentiel
- `.parallelStream()` : pipeline parallèle, multi-thread

***

## V. Exemples de code détaillés

### Exemple 1 – Filtrer et transformer une liste

```java
List<String> noms = Arrays.asList("ali", "asma", "reda", "amina");
List<String> resultat = noms.stream()
    .filter(n -> n.length() > 3)
    .map(String::toUpperCase)
    .toList());
// resultat = [ASMA, REDA, AMINA]
```


### Exemple 2 – Somme des entiers pairs

```java
int sommePairs = IntStream.range(1, 11)
    .filter(n -> n % 2 == 0)
    .sum(); // 2+4+6+8+10 = 30
```


### Exemple 3 – Stream parallèle

```java
long count = listDeLogs.parallelStream()
    .filter(log -> log.contains("ERROR"))
    .count();
```


***

## VI. Cas d’usage informatique et réseaux

### Traitement de logs système

```java
List<String> erreurs = logs.stream()
    .filter(l -> l.startsWith("[ERR]"))
    .collect(Collectors.toList());
```


### Extraction d’adresses IP

```java
List<String> ips = lignes.stream()
    .map(l -> extraireIP(l))
    .filter(Objects::nonNull)
    .distinct()
    .collect(Collectors.toList());
```


### Statistiques sur flux

```java
DoubleSummaryStatistics stats = values.stream()
    .collect(Collectors.summarizingDouble(Double::doubleValue));
// stats.getAverage(), stats.getMax(), stats.getMin()...
```


***

## VII. Parallélisme avec les Streams

Le parallélisme avec les Streams permet d’exploiter plusieurs cœurs du processeur sans écrire explicitement la gestion des threads.

- `.parallelStream()` sur une collection : exécute chaque sous-tâche dans un thread distinct via le ForkJoinPool.
- S’utilise pour de **gros volumes de données** ou des tâches longues ; la parallélisation a un coût si l’opération est simple ou la collection petite.

**Exemple pratique :**

```java
long t1 = System.currentTimeMillis();
long count = clients.parallelStream()
    .filter(c -> c.getBalance() > 10000)
    .count();
long t2 = System.currentTimeMillis();
System.out.println("Temps d'exécution : " + (t2-t1) + " ms");
```

**Points de vigilance :**

- Les résultats peuvent être dans un ordre non déterminé.
- Les opérations doivent être indépendantes les unes des autres (pas d’effet de bord).
- Tester si la parallélisation apporte vraiment un gain avant de généraliser.

***

## VIII. Traitements avancés avec Streams

- **Streams infinis :**

```java
Stream<Integer> entiers = Stream.iterate(0, n -> n + 1).limit(10);
entiers.forEach(System.out::println); // Affiche 0 à 9
```

- **Agrégations avancées :**

```java
Map<Genre, List<Employe>> parGenre = employes.stream()
    .collect(Collectors.groupingBy(Employe::getGenre));
```

- **Combinaison d’opérations :**

```java
public List<ExpensiveResult> optimizedPipeline(List<RawData> data) {
    return data.stream()
        .filter(this::isValid)
        .filter(this::isRelevant)
        .map(this::expensiveTransform)
        .collect(Collectors.toList());
}
```

- **flatMap() pour aplatir :**

```java
List<List<String>> groupes = ...;
groupes.stream().flatMap(List::stream).collect(Collectors.toList());
```


***

## IX. Bonnes pratiques et limites

- Privilégier les streams pour le traitement en pipeline, surtout sur de grands ensembles de données.
- Éviter les side effects : ne pas modifier de variables extérieures dans les expressions lambda.
- Bien tester le comportement parallèle et l’utiliser à bon escient.
- Les streams ne se consomment qu’une seule fois.

***

## X. Exercices pratiques

### Exercice 1

Obtenir une liste de tous les noms en majuscules contenant plus de 5 lettres à partir d’une liste de chaînes `noms`.

### Exercice 2

Récupérer uniquement les nombres impairs, les trier en ordre décroissant et les collecter dans une nouvelle liste.

### Exercice 3

À partir d’une collection d’objets `Utilisateur` (avec `prenom`, `nom`, `age`), obtenir la liste triée des prénoms uniques des utilisateurs de moins de 25 ans.

### Exercice 4

Extraire les logs contenant « CRITICAL », supprimer les doublons et afficher le nombre total.

### Exercice 5

Calculer la moyenne des prix des produits de la catégorie « Informatique » dans une liste de `Produit`.

### Exercice 6

Filtrer les commandes `Commande` d’un montant >1000 €, extraire les dates, les trier croissants, récupérer dans une liste.

### Exercice 7

À partir d’un flux de notes d’étudiants, récupérer la note maximale, minimale et la moyenne.

### Exercice 8

Créer une Map qui associe chaque service à la liste des noms d’employés qui en font partie.

### Exercice 9

Récupérer toutes les chaînes contenant `'a'`, les trier par longueur croissante, supprimer les doublons et afficher le résultat.

### Exercice 10

Extraire tous les titres de livres publiés avant 2000, les trier et afficher la liste finale.

***

## XI. Corrigés des exercices

### Exercice 1

```java
List<String> result = noms.stream()
    .filter(n -> n.length() > 5)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```


### Exercice 2

```java
List<Integer> result = nombres.stream()
    .filter(n -> n % 2 != 0)
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
```


### Exercice 3

```java
List<String> result = utilisateurs.stream()
    .filter(u -> u.getAge() < 25)
    .map(Utilisateur::getPrenom)
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```


### Exercice 4

```java
long count = logs.stream()
    .filter(l -> l.contains("CRITICAL"))
    .distinct()
    .count();
```


### Exercice 5

```java
OptionalDouble moyenne = produits.stream()
    .filter(p -> p.getCategorie().equals("Informatique"))
    .mapToDouble(Produit::getPrix)
    .average();
```


### Exercice 6

```java
List<LocalDate> dates = commandes.stream()
    .filter(c -> c.getMontant() > 1000)
    .map(Commande::getDate)
    .sorted()
    .collect(Collectors.toList());
```


### Exercice 7

```java
IntSummaryStatistics stats = notes.stream()
    .mapToInt(Integer::intValue)
    .summaryStatistics();
// stats.getMax(), stats.getMin(), stats.getAverage()
```


### Exercice 8

```java
Map<String, List<String>> map = employes.stream()
    .collect(Collectors.groupingBy(
        Employe::getService,
        Collectors.mapping(Employe::getNom, Collectors.toList())
    ));
```


### Exercice 9

```java
List<String> result = chaines.stream()
    .filter(s -> s.contains("a"))
    .distinct()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());
```


### Exercice 10

```java
List<String> titres = livres.stream()
    .filter(l -> l.getAnnee() < 2000)
    .map(Livre::getTitre)
    .sorted()
    .collect(Collectors.toList());
```


***

## XII. Déclarations et tests pour Exercice 3 et 5 (avec noms marocains)

### Classe Utilisateur

```java
public class Utilisateur {
    private String prenom;
    private String nom;
    private int age;

    public Utilisateur(String prenom, String nom, int age) {
        this.prenom = prenom;
        this.nom = nom;
        this.age = age;
    }
    public String getPrenom() { return prenom; }
    public String getNom() { return nom; }
    public int getAge() { return age; }
}
```


### Test UtilisateurStream

```java
import java.util.*;
import java.util.stream.*;

public class TestUtilisateurStream {
    public static void main(String[] args) {
        List<Utilisateur> utilisateurs = Arrays.asList(
            new Utilisateur("Yassine", "El Amrani", 22),
            new Utilisateur("Fatima", "Benhadda", 28),
            new Utilisateur("Yassine", "Ait Lahcen", 19),
            new Utilisateur("Jamila", "Kabbaj", 24),
            new Utilisateur("Omar", "Berrada", 22),
            new Utilisateur("Rania", "Ouazzani", 23),
            new Utilisateur("Ahmed", "Maarouf", 21)
        );

        List<String> result = utilisateurs.stream()
            .filter(u -> u.getAge() < 25)
            .map(Utilisateur::getPrenom)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        System.out.println(result); // [Ahmed, Jamila, Omar, Rania, Yassine]
    }
}
```


### Classe Produit

```java
public class Produit {
    private String nom;
    private String categorie;
    private double prix;

    public Produit(String nom, String categorie, double prix) {
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
    }
    public String getNom() { return nom; }
    public String getCategorie() { return categorie; }
    public double getPrix() { return prix; }
}
```


### Test ProduitStream

```java
import java.util.*;
import java.util.stream.*;

public class TestProduitStream {
    public static void main(String[] args) {
        List<Produit> produits = Arrays.asList(
            new Produit("PC Dell Mohammedia", "Informatique", 10500.0),
            new Produit("Souris Casablanca", "Informatique", 190.0),
            new Produit("Table berbère", "Mobilier", 700.0),
            new Produit("Imprimante Rabat", "Informatique", 900.0),
            new Produit("Chaise Essaouira", "Mobilier", 350.0),
            new Produit("Routeur Maroc Telecom", "Informatique", 450.0)
        );

        OptionalDouble moyenne = produits.stream()
            .filter(p -> p.getCategorie().equals("Informatique"))
            .mapToDouble(Produit::getPrix)
            .average();

        if (moyenne.isPresent()) {
            System.out.printf("Prix moyen Informatique : %.2f\n", moyenne.getAsDouble());
        } else {
            System.out.println("Aucun produit en catégorie Informatique.");
        }
        // Exemple d'affichage : Prix moyen Informatique : 2610.00
    }
}
```


***

## XIII. Conclusion et pistes d’approfondissement

Les Streams modernisent le traitement des collections et flux de données : performance, expressivité, parallélisme et concision. La maîtrise des opérations, leur enchaînement et la pratique sur des cas authentiques sont essentielles pour exploiter la puissance de Java 8+ en ingénierie informatique et réseaux.

**Pour aller plus loin** :

- Reactive Streams (Java 9+)
- Tuning avancé ForkJoinPool
- Intégration API réseau/base de données en flux

***
