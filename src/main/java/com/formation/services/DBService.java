package com.formation.services;

import com.formation.exceptions.MetierException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DBService {

    private static final Logger logger = LogManager.getLogger("DBService");

    // Singleton
    private static DBService instance;

    private static String ipAddress = "127.0.0.1";
    private static Connection connection;

    public static void configure(String ip) throws MetierException{
        if(ip == null || ip.isEmpty()){
            throw new MetierException("L ip ne peut être vide où nul");
        }
        ipAddress = ip;
        logger.info("L'adresse de la base de données est fixée à " + ipAddress);
    }

    public static DBService getInstance() {
        if (instance == null) {
            instance = new DBService();
        }
        return instance;
    }

    private DBService() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + ipAddress + "/ecommerce?user=root&password=formation&useSSL=false");
        } catch (ClassNotFoundException e) {
            logger.error("Impossible de trouver le driver jdbc : " + e.getMessage(), e);
        } catch (SQLException e) {
            logger.error("Impossible de se connecter à la base : " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public PreparedStatement prepareStatement(String requete) throws SQLException {
        return connection.prepareStatement(requete);
    }

    public ResultSet executeSelect(String requete) throws SQLException {
        return createStatement().executeQuery(requete);
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Une erreur est survenue à la fermeture de la connexion : " + e.getMessage(), e);
            }
        }
    }

    public static void reset() {
        if (instance != null) {
            close();
            instance = null;
        }
    }
}
