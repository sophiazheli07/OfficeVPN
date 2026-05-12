package com.vpn.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL  = "jdbc:mysql://localhost:3306/office_vpn";
    private static final String USER = "root";
    private static final String PASS = resolvePassword();

    private static String resolvePassword() {
        String envPassword = System.getenv("OFFICE_VPN_DB_PASSWORD");
        if (envPassword != null && !envPassword.isBlank()) {
            return envPassword;
        }

        String propertyPassword = System.getProperty("office.vpn.db.password");
        if (propertyPassword != null && !propertyPassword.isBlank()) {
            return propertyPassword;
        }

        return "";
    }

    public static boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if a matching user is found, false if not

        } catch (SQLException e) {
            System.out.println("Database Connection Error!");
            e.printStackTrace();
            return false;
        }
    }
    // create a method which returns all the users to retrieve all the users in the db as well as their passwords
    // this is for debugging purposes only, to see if the queries are working correctly and to see what data is actually in the database
    public static void printAllUsers() {    
        String query = "SELECT username, password FROM users ORDER BY username";
        String dbQuery = "SELECT DATABASE() AS db_name";
        

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (PreparedStatement dbStmt = conn.prepareStatement(dbQuery);
                 ResultSet dbRs = dbStmt.executeQuery()) {
                if (dbRs.next()) {
                    System.out.println("Connected database: " + dbRs.getString("db_name"));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("User: " + rs.getString("username") + " | Password: " + rs.getString("password"));
                }
                System.out.println("Total users retrieved: " + count);
            }

        } catch (SQLException e) {
            System.out.println("Database Connection Error!");
            e.printStackTrace();
        }
    }

    public static void debugLogin(String username, String password) {
    // Query 1: check if username exists at all
    String queryUser = "SELECT * FROM users WHERE username = ?";
    // Query 2: check exact match with password
    String queryBoth = "SELECT * FROM users WHERE username = ? AND password = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

        // Step 1: Does the username exist?
        try (PreparedStatement stmt = conn.prepareStatement(queryUser)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("Username FOUND: " + rs.getString("username"));
                    System.out.println("Password in DB: [" + rs.getString("password") + "]");
                    System.out.println("Password entered: [" + password + "]");
                }
                if (!found) {
                    System.out.println("Username NOT FOUND in DB: [" + username + "]");
                }
            }
        }

        // Step 2: Does username + password match?
        try (PreparedStatement stmt = conn.prepareStatement(queryBoth)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login MATCH: success");
                } else {
                    System.out.println("Login FAILED: password does not match");
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}