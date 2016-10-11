package com.formation.services;

import com.formation.exceptions.MetierException;
import com.formation.models.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientService {

    private static final Logger log = LogManager.getLogger(ClientService.class);

    public static Client creerClient(String email, String nom, String prenom) throws MetierException {

        log.info("creerClient : {} / {} / {}", email, nom, prenom);

        if (email == null || email.isEmpty()) {
            throw new MetierException("L'email ne peut être vide");
        } else if (!isValid(email)) {
            throw new MetierException("L'email n'est pas valide");
        }
        if (nom == null || nom.isEmpty()) {
            throw new MetierException("Le nom ne peut être vide");
        }
        if (prenom == null || prenom.isEmpty()) {
            throw new MetierException("Le prenom ne peut être vide");
        }

        Client client = new Client();
        client.id = UUID.randomUUID().toString();
        client.email = email;
        client.nom = nom;
        client.prenom = prenom;
        return client;
    }

    public static void enregistrer(Client client) throws MetierException {
        if (client == null) {
            throw new MetierException("Le client ne peut être null");
        }
        try {
            DBService dbService = DBService.getInstance();

            String requete = "INSERT INTO Client (`id`, `email`, `nom`, `prenom`) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, client.id);
            preparedStatement.setString(2, client.email);
            preparedStatement.setString(3, client.nom);
            preparedStatement.setString(4, client.prenom);
            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
    }

    public static Client getClient(String idClient) {
        Client client = null;
        try {
            DBService dbService = DBService.getInstance();

            String requete = "SELECT id, email, nom, prenom FROM Client WHERE id = ?";
            PreparedStatement preparedStatement = dbService.prepareStatement(requete);
            preparedStatement.setString(1, idClient);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                client = new Client();
                client.id = result.getString("id");
                client.email = result.getString("email");
                client.nom = result.getString("nom");
                client.prenom = result.getString("prenom");
            }
            result.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
        return client;
    }

    public static List<Client> lister() {
        List<Client> clients = new ArrayList<>();
        try {

            DBService dbService = DBService.getInstance();

            String requete = "SELECT id, email, nom, prenom FROM Client";
            ResultSet result = dbService.executeSelect(requete);
            while (result.next()) {
                Client client = new Client();
                client.id = result.getString("id");
                client.email = result.getString("email");
                client.nom = result.getString("nom");
                client.prenom = result.getString("prenom");
                clients.add(client);
            }
            result.close();
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à la base : " + e.getMessage());
        }
        return clients;
    }

    private static boolean isValid(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
