package com.formation.services;

import com.formation.exceptions.MetierException;
import com.formation.models.Client;
import com.formation.models.Panier;
import com.formation.models.Produit;
import com.formation.models.ProduitAjouteAuPanier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class PanierService {

    public static Panier getPanier(Client client) {

        Panier panier = null;
        try {
            DBService dbService = DBService.getInstance();

            String requete = "SELECT id, date FROM Panier WHERE idClient = ?";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, client.id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                panier = new Panier();
                panier.id = result.getString("id");
                panier.client = client;
                panier.date = result.getTimestamp("date").toLocalDateTime();

                // TODO allez chercher les produits du panier

//                String requeteProduits = "SELECT idProduit, quantite FROM " +
//                        "ProduitAjouteAuPanier WHERE idPanier = ?";
//                PreparedStatement preparedStatementProduits = dbService.prepareStatement(requeteProduits);
//                preparedStatementProduits.setString(1, panier.id);
//                ResultSet resultProduit = preparedStatementProduits.executeQuery();
//                while (resultProduit.next()) {
//                    Produit produit = ProduitService.getProduit(resultProduit.getString("idProduit"));
//                    ProduitAjouteAuPanier produitAjouteAuPanier = new ProduitAjouteAuPanier(produit);
//                    produitAjouteAuPanier.quantite = result.getInt("quantite");
//                    panier.produits.add(produitAjouteAuPanier);
//                }
            }
            result.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
        if (panier == null) {
            panier = creerPanier(client);
            try {
                enregistrer(panier);
            } catch (MetierException e) {
                e.printStackTrace();
            }
        }
        return panier;
    }

    private static Panier creerPanier(Client client) {
        Panier panier = new Panier();
        panier.id = UUID.randomUUID().toString();
        panier.client = client;
        panier.produits = new ArrayList<>();
        panier.date = LocalDateTime.now();
        return panier;
    }

    public static void enregistrer(Panier panier) throws MetierException {
        if (panier == null) {
            throw new MetierException("Le panier ne peut être null");
        }
        try {
            DBService dbService = DBService.getInstance();

            String requete = "INSERT INTO Panier (`id`, `idClient`, `date`) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, panier.id);
            preparedStatement.setString(2, panier.client.id);
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(panier.date));
            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
    }

    public static ProduitAjouteAuPanier getProduitAjouteAuPanier(Panier panier, Produit produit) {
        ProduitAjouteAuPanier produitAjouteAuPanier = null;
        try {
            DBService dbService = DBService.getInstance();

            String requete = "SELECT quantite FROM ProduitAjouteAuPanier WHERE idPanier = ? AND idProduit = ?";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, panier.id);
            preparedStatement.setString(2, produit.id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                produitAjouteAuPanier = new ProduitAjouteAuPanier(produit);
                produitAjouteAuPanier.quantite = result.getInt("quantite");
            }
            result.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
        return produitAjouteAuPanier;
    }

    public static void ajouterProduit(Panier panier, Produit produit) throws MetierException {
        if (panier == null) {
            throw new MetierException("Le panier ne peut etre null");
        }
        if (produit == null) {
            throw new MetierException("Le produit ne peut etre null");
        }


        DBService dbService = DBService.getInstance();
        try {
            ProduitAjouteAuPanier produitAjouteAuPanier = getProduitAjouteAuPanier(panier, produit);
            if (produitAjouteAuPanier != null) {
                String requeteUpdate = "UPDATE ProduitAjouteAuPanier SET quantite = ? WHERE idPanier = ? AND idProduit = ?";
                PreparedStatement preparedStatement = dbService.prepareStatement(requeteUpdate);
                preparedStatement.setInt(1, ++produitAjouteAuPanier.quantite);
                preparedStatement.setString(2, panier.id);
                preparedStatement.setString(3, produit.id);
                preparedStatement.execute();
            } else {
                String requeteInsert = "INSERT INTO ProduitAjouteAuPanier (idPanier, idProduit, quantite) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = dbService.prepareStatement(requeteInsert);
                preparedStatement.setString(1, panier.id);
                preparedStatement.setString(2, produit.id);
                preparedStatement.setInt(3, 1);
                preparedStatement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
