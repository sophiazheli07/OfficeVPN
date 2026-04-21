package com.vpn.client;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        Label label = new Label("Office VPN Login");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        Button loginBtn = new Button("Connect");
        loginBtn.setMaxWidth(100);

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            if(user.equals("admin") && pass.equals("12345")) {
                System.out.println("Login successful! Connecting to VPN...");
                label.setText("Status: Connected");
                label.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                DashboardWindow dashboard = new DashboardWindow();
                dashboard.show(primaryStage, user);
            } else {
                System.out.println("Login failed! Please try again.");
                label.setText("Status: Login Failed");
                label.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, usernameField, passwordField, loginBtn);

        Scene scene = new Scene(layout, 300, 450);
        primaryStage.setTitle("VPN Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
