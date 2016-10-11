package com.formation.models;

import java.time.LocalDateTime;
import java.util.List;

public class Panier {

    public String id;
    public Client client;
    public List<ProduitAjouteAuPanier> produits;
    public LocalDateTime date;

}

