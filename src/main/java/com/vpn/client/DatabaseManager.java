package com.vpn.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/office_vpn"; 
    private static final String USER = "root"; 
    private static final String PASS = "Sophia.zheli07";

    public static boolean validateLogin(String username, String password){
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            boolean loginSuccessful = rs.next(); // returns true if a record is found

            return loginSuccessful;

        } catch (SQLException e) {
            System.out.println("Database Connection Error!");
            e.printStackTrace();
            return false;
        }
    }
}

