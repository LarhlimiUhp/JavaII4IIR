package com.examen.stock;

import com.examen.stock.exception.StockException;
import com.examen.stock.model.Produit;
import com.examen.stock.service.GestionnaireStock;

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
