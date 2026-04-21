package com.vpn.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
public class LoginController {

    @FXML
    private TextField usernameField;


    @FXML
    private Button loginButton;

    @FXML
    public void handleLogin(ActionEvent event) {
        System.out.println("Attempting to connect to office VPN");    
    }
}
