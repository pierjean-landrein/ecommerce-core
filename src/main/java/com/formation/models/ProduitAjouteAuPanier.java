package com.formation.models;

public class ProduitAjouteAuPanier {

    public Produit produit;
    public Integer quantite;

    public ProduitAjouteAuPanier(Produit produit) {
        this.produit = produit;
        this.quantite = 1;
    }
}
