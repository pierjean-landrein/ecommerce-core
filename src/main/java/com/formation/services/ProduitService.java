package com.formation.services;

import com.formation.exceptions.MetierException;
import com.formation.models.Produit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProduitService {

    private static final Logger log = LogManager.getLogger(ProduitService.class);

    public static Produit creerProduit(String nom, Float prixUnitaire) throws MetierException {

        log.info("creerProduit : {} / {}", nom, prixUnitaire);

        if (nom == null || nom.isEmpty()) {
            throw new MetierException("Le nom ne peut être vide");
        }
        if (prixUnitaire == null || prixUnitaire == 0) {
            throw new MetierException("Le prix Unitaire ne peut etre égal à 0");
        }

        Produit produit = new Produit();
        produit.id = UUID.randomUUID().toString();
        produit.nom = nom;
        produit.prixUnitaire = prixUnitaire;
        return produit;
    }

    public static void enregistrer(Produit produit) throws MetierException {
        if (produit == null) {
            throw new MetierException("Le produit ne peut etre null");
        }
        try {
            DBService dbService = DBService.getInstance();

            String requete = "INSERT INTO Produit (`id`, `nom`, `prixUnitaire`) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, produit.id);
            preparedStatement.setString(2, produit.nom);
            preparedStatement.setFloat(3, produit.prixUnitaire);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
    }

    public static Produit getProduit(String idProduit) {
        Produit produit = null;
        try {
            DBService dbService = DBService.getInstance();

            String requete = "SELECT id, nom, prixUnitaire FROM Produit WHERE id = ?";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, idProduit);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                produit = new Produit();
                produit.id = result.getString("id");
                produit.nom = result.getString("nom");
                produit.prixUnitaire = result.getFloat("prixUnitaire");
            }
            result.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
        return produit;
    }

    public static List<Produit> lister() {
        List<Produit> produits = new ArrayList<>();
        try {
            DBService dbService = DBService.getInstance();

            String requete = "SELECT id, nom, prixUnitaire FROM Produit";
            ResultSet result = dbService.executeSelect(requete);
            while (result.next()) {
                Produit produit = new Produit();
                produit.id = result.getString("id");
                produit.nom = result.getString("nom");
                produit.prixUnitaire = result.getFloat("prixUnitaire");
                produits.add(produit);
            }
            result.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
        return produits;
    }
}
