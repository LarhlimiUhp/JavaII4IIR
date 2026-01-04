package com.examen.stock.service;

import com.examen.stock.exception.StockException;
import com.examen.stock.model.Produit;
import com.examen.stock.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionnaireStock<T extends Produit> implements Repository<T> {
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
