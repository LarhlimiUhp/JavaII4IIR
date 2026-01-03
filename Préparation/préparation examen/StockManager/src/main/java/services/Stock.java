package services;

import models.Produit;
import exceptions.IdInvalideException;
import java.util.*;

public class Stock<T extends Produit> {
    private Map<String, T> produits = new HashMap<>();

    public void ajouter(T produit) throws IdInvalideException {
        if (produit.getId() == null || produit.getId().trim().isEmpty()) {
            throw new IdInvalideException("L'ID du produit ne peut pas être vide ou nul.");
        }
        if (produits.containsKey(produit.getId())) {
            throw new IdInvalideException("Un produit avec l'ID " + produit.getId() + " existe déjà.");
        }
        produits.put(produit.getId(), produit);
    }

    public T rechercher(String id) {
        return produits.get(id);
    }

    public List<T> listerTout() {
        return new ArrayList<>(produits.values());
    }

    public void supprimer(String id) {
        produits.remove(id);
    }
}
