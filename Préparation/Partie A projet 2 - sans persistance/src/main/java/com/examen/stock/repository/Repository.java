package com.examen.stock.repository;

import com.examen.stock.exception.StockException;
import java.util.List;

public interface Repository<T> {
    void ajouter(T element);
    List<T> listerTout();
    T trouverParNom(String nom) throws StockException;
}
