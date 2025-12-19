# Tp Hibernete

## 1. Mise en place de l’environnement

### Objectifs

- Créer un projet Maven Java sous IntelliJ IDEA avec le groupId `ma.emsi.tp`.
- Préparer l’utilisation de Hibernate avec mapping XML et MySQL.[^1][^2]


### Consignes

- Créer un projet Maven : `File | New | Project | Maven`, `groupId = ma.emsi.tp`, `artifactId = tp-hibernate-mysql`. [^1]
- Vérifier que Java 17 est bien configuré dans **Project Structure | Project SDK**. [^3]

***

## 2. Déploiement de MySQL avec Docker (via IntelliJ)

### Objectifs

- Lancer un conteneur MySQL accessible sur `localhost:3306`.
- Préparer l’import du script SQL fourni.[^4][^5]


### Consignes pas à pas

- Activer le plugin Docker : `File | Settings | Plugins`, installer **Docker**, redémarrer. [^6]
- Configurer Docker : `File | Settings | Build, Execution, Deployment | Docker` → ajouter une connexion Docker locale. [^6]
- Dans la fenêtre **Services**, onglet Docker, faire **Pull** de l’image `mysql:8.0`.[^4]
- Créer un conteneur nommé `mysql-hibernate-tp` avec : port `3306:3306`, `MYSQL_ROOT_PASSWORD=root`, `MYSQL_DATABASE=gc2000`.[^4]
- Importer le script SQL : `docker exec -i mysql-hibernate-tp mysql -uroot -proot gc2000 < script_gc2000.sql`.

**Livrable** : capture d’écran du conteneur MySQL en cours d’exécution dans IntelliJ.

***

## 3. Datasource MySQL dans IntelliJ

### Objectifs

- Créer une datasource MySQL pointant vers le conteneur Docker.
- Vérifier le schéma créé par le script SQL.[^7][^8]


### Consignes

- Ouvrir la fenêtre **Database**, puis **+ | Data Source | MySQL**. [^7]
- Paramètres : `host = localhost`, `port = 3306`, `user = root`, `password = root`, `database = gc2000`, puis **Test Connection** (télécharger le driver si demandé).[^7]
- Exécuter un `SELECT * FROM <une_table>;` pour vérifier que les données du script sont présentes.[^9]

**Livrable** : capture d’écran de la connexion réussie et d’une requête SQL simple.

***

## 4. Configuration Hibernate côté XML

### Objectifs

- Configurer Hibernate à partir de fichiers XML : `hibernate.cfg.xml` et fichiers de mapping `*.hbm.xml`.
- Comprendre le lien entre configuration, mappings et classes Java.[^2][^1]


### 4.1 Dépendances Maven

Dans `pom.xml` :

```xml
<groupId>ma.emsi.tp</groupId>
<artifactId>tp-hibernate-mysql</artifactId>

<dependencies>
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.5.2.Final</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.0.0</version>
    </dependency>

    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.13</version>
    </dependency>
</dependencies>
```


### 4.2 Fichier `hibernate.cfg.xml`

Créer `src/main/resources/hibernate.cfg.xml` :

```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- Connexion MySQL -->
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">
            jdbc:mysql://localhost:3306/gc2000?useSSL=false&amp;serverTimezone=UTC
        </property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">root</property>

        <!-- Propriétés Hibernate -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="show_sql">true</property>
        <property name="format_sql">true</property>
        <property name="hbm2ddl.auto">none</property>

        <!-- Mappings XML -->
        <mapping resource="ma/emsi/tp/entities/Client.hbm.xml"/>
        <mapping resource="ma/emsi/tp/entities/Commande.hbm.xml"/>
        <mapping resource="ma/emsi/tp/entities/LigneCommande.hbm.xml"/>
    </session-factory>
</hibernate-configuration>
```

**Point didactique** : insister sur le rôle du `mapping resource="...hbm.xml"` qui rend les classes persistantes sans annotations.[^10][^2]

***

## 5. Mapping XML des entités

### Objectifs

- Dériver les fichiers `*.hbm.xml` à partir du schéma SQL.
- Configurer les relations `one-to-many` et `many-to-one` en XML.[^1][^2]

On suppose les tables `client`, `commande`, `ligne_commande` (schéma identique à la version précédente).

### 5.1 Classes Java (sans annotations)

```java
package ma.emsi.tp.entities;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private Long id;
    private String nom;
    private String email;
    private List<Commande> commandes = new ArrayList<>();

    public Client() {}
    public Client(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    // getters/setters ...
}
```

```java
package ma.emsi.tp.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private Long id;
    private LocalDate date;
    private Client client;
    private List<LigneCommande> lignes = new ArrayList<>();

    public Commande() {}
    public Commande(LocalDate date, Client client) {
        this.date = date;
        this.client = client;
    }

    // getters/setters ...
}
```

```java
package ma.emsi.tp.entities;

public class LigneCommande {
    private Long id;
    private String produit;
    private int quantite;
    private double prixUnitaire;
    private Commande commande;

    public LigneCommande() {}
    public LigneCommande(String produit, int quantite, double prixUnitaire, Commande commande) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.commande = commande;
    }

    // getters/setters ...
}
```


### 5.2 Fichier `Client.hbm.xml`

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping package="ma.emsi.tp.entities">
    <class name="Client" table="client">
        <id name="id" column="id_client" type="long">
            <generator class="identity"/>
        </id>

        <property name="nom" column="nom" type="string" not-null="true"/>
        <property name="email" column="email" type="string" not-null="true"/>

        <set name="commandes" inverse="true" cascade="all-delete-orphan">
            <key column="id_client"/>
            <one-to-many class="Commande"/>
        </set>
    </class>
</hibernate-mapping>
```


### 5.3 Fichier `Commande.hbm.xml`

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping package="ma.emsi.tp.entities">
    <class name="Commande" table="commande">
        <id name="id" column="id_commande" type="long">
            <generator class="identity"/>
        </id>

        <property name="date" column="date" type="java.time.LocalDate"/>

        <many-to-one name="client" class="Client" column="id_client" not-null="true"/>

        <set name="lignes" inverse="true" cascade="all-delete-orphan">
            <key column="id_commande"/>
            <one-to-many class="LigneCommande"/>
        </set>
    </class>
</hibernate-mapping>
```


### 5.4 Fichier `LigneCommande.hbm.xml`

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping package="ma.emsi.tp.entities">
    <class name="LigneCommande" table="ligne_commande">
        <id name="id" column="id_ligne" type="long">
            <generator class="identity"/>
        </id>

        <property name="produit" column="produit" type="string" not-null="true"/>
        <property name="quantite" column="quantite" type="int" not-null="true"/>
        <property name="prixUnitaire" column="prix_unitaire" type="double" not-null="true"/>

        <many-to-one name="commande" class="Commande" column="id_commande" not-null="true"/>
    </class>
</hibernate-mapping>
```

**Insistance pédagogique** :

- Montrer que toute la **métadonnée de mapping** (types, colonnes, relations) est centralisée dans les fichiers XML, sans annotations dans le code.[^2][^10]
- Faire comparer une version annotée et la version XML pour bien voir les correspondances.

***

## 6. Utilitaire Hibernate et DAO

### Objectifs

- Créer un `SessionFactory` à partir de `hibernate.cfg.xml`.
- Encapsuler les opérations CRUD dans des DAO simples.[^10]


### 6.1 Classe `HibernateUtil`

```java
package ma.emsi.tp.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
```


### 6.2 Exemple `ClientDao`

```java
package ma.emsi.tp.dao;

import ma.emsi.tp.entities.Client;
import ma.emsi.tp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClientDao {

    public Client save(Client client) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(client);
            tx.commit();
            return client;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Client findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Client.class, id);
        }
    }

    public List<Client> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Client", Client.class).list();
        }
    }

    public Client update(Client client) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(client);
            tx.commit();
            return client;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Client c = session.get(Client.class, id);
            if (c != null) session.remove(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
```


***

## 7. Classe `Main` : scénario complet

### Objectifs

- Tester le mapping XML via un scénario CRUD complet et une requête avec jointure.
- Vérifier la cohérence des relations définies dans les fichiers `hbm.xml`.[^20]


### Consignes

```java
package ma.emsi.tp.app;

import ma.emsi.tp.dao.ClientDao;
import ma.emsi.tp.entities.Client;
import ma.emsi.tp.entities.Commande;
import ma.emsi.tp.entities.LigneCommande;
import ma.emsi.tp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        ClientDao clientDao = new ClientDao();

        // 1. Insertion d'un client
        Client client = new Client("Dupont", "dupont@example.com");
        clientDao.save(client);

        // 2. Création d'une commande + lignes (relation one-to-many)
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Client managedClient = session.get(Client.class, client.getId());
        Commande commande = new Commande(LocalDate.now(), managedClient);

        LigneCommande l1 = new LigneCommande("PC Portable", 1, 800.0, commande);
        LigneCommande l2 = new LigneCommande("Souris", 2, 20.0, commande);

        commande.getLignes().add(l1);
        commande.getLignes().add(l2);
        managedClient.getCommandes().add(commande);

        session.persist(commande);
        tx.commit();
        session.close();

        // 3. Lecture
        Client c = clientDao.findById(client.getId());
        System.out.println("Client : " + c.getNom() + " - " + c.getEmail());

        // 4. Mise à jour
        c.setEmail("dupont.new@example.com");
        clientDao.update(c);

        // 5. Requête avec jointure (commandes + lignes)
        Session session2 = HibernateUtil.getSessionFactory().openSession();
        List<Commande> commandes = session2.createQuery(
                "select distinct cmd from Commande cmd " +
                        "join fetch cmd.lignes " +
                        "where cmd.client.id = :idClient", Commande.class)
                .setParameter("idClient", client.getId())
                .list();
        session2.close();

        for (Commande cmd : commandes) {
            System.out.println("Commande #" + cmd.getId() + " du " + cmd.getDate());
            cmd.getLignes().forEach(l ->
                    System.out.println("  - " + l.getProduit() +
                            " x" + l.getQuantite() +
                            " = " + (l.getQuantite() * l.getPrixUnitaire()) + " EUR"));
        }

        // 6. Suppression
        clientDao.delete(client.getId());

        HibernateUtil.shutdown();
    }
}
```

**Livrable** : capture d’écran de la console montrant la séquence CRUD + jointure, et commentaires d’étudiant sur les liens entre XML, classes et tables.
<span style="display:none">[^11][^12][^13][^14][^15][^16][^17][^18][^19][^21][^22][^23][^24][^25][^26][^27]</span>

<div align="center">⁂</div>

[^1]: https://mkyong.com/hibernate/quick-start-maven-hibernate-mysql-example/

[^2]: https://docs.hibernate.org/orm/3.3/reference/en/html/mapping.html

[^3]: https://www.javaguides.net/2021/08/hibernate-example-with-mysql-database.html

[^4]: https://www.jetbrains.com/help/idea/running-a-dbms-image.html

[^5]: https://www.docker.com/blog/getting-started-with-visual-studio-code-and-intellij-idea-docker-plugins/

[^6]: https://www.jetbrains.com/help/idea/docker.html

[^7]: https://www.jetbrains.com/help/idea/mysql.html

[^8]: https://www.jetbrains.com/help/idea/managing-data-sources.html

[^9]: https://qanswer.space/questions/integrating-intellij-with-a-local-mysql-database-a-step-by-step-guide

[^10]: https://docs.hibernate.org/orm/4.1/quickstart/en-US/html/ch02.html

[^11]: https://stackoverflow.com/questions/17992323/how-to-create-hbm-xml-file-for-mysql-view-and-how-to-add-it-in-hibernate-configu

[^12]: https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/An-example-hibernatecfgxml-for-MySQL-8-and-Hibernate-5

[^13]: https://www.geeksforgeeks.org/advance-java/hibernate-mapping-set-using-xml/

[^14]: https://github.com/Dohbedoh/spring-hibernate-xml

[^15]: https://thorben-janssen.com/jpa-persistence-xml/

[^16]: https://stackoverflow.com/questions/9942767/how-to-make-a-hibernate-mapping-file-in-intellij-11-ultimate

[^17]: https://jakarta.ee/learn/specification-guides/persistence-explained/

[^18]: https://www.youtube.com/watch?v=QJddHc41xrM

[^19]: https://fr.scribd.com/doc/40428093/Hibernate

[^20]: https://payara.fish/blog/getting-started-with-jakarta-ee-9-jakarta-persistence-api-jpa/

[^21]: https://www.geeksforgeeks.org/java/hibernate-table-per-hierarchy-using-xml-file/

[^22]: https://openmrs.atlassian.net/wiki/spaces/docs/pages/25462957/Module+Hibernate+Mapping+Files

[^23]: https://stackoverflow.com/questions/26187180/can-i-declare-mappings-in-the-persistence-xml

[^24]: https://www.alwa.info/2017/How-to-install-Hibernate-in-IntelliJ-IDEA.html

[^25]: https://www.centron.de/en/tutorial/hibernate-one-to-one-mapping-example-with-annotation-and-xml/

[^26]: https://docs.redhat.com/en/documentation/red_hat_jboss_enterprise_application_platform/7.4/html/development_guide/java_persistence_api

[^27]: https://intellij-support.jetbrains.com/hc/en-us/community/posts/206291199-Generate-Hibernate-Mapping-File

