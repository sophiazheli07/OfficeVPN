package com.vpn.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardWindow {
    private Socket vpnSocket;
    private PrintWriter out;
    private BufferedReader in;
    private double dataUsed = 0.0;
    private javafx.animation.Timeline trafficTimer;

    private void startVpnTunnel(Label statusLabel, Button actionBtn, Label dataLabel) {
        Task<Void> connectTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                vpnSocket = new Socket("localhost", 8080);
                out = new PrintWriter(vpnSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(vpnSocket.getInputStream()));
                
                out.println("CLIENT_START_SESSION");
                return null;
            }
        };

        connectTask.setOnSucceeded(ev -> {
            statusLabel.setText("Status: Connected & Secure");
            statusLabel.setStyle("-fx-text-fill: green;");
            actionBtn.setText("Disconnect");
        });
        
        new Thread(connectTask).start();

        trafficTimer = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> {
                // data simulation
                dataUsed += 0.1 + (Math.random() * 0.4);
                dataLabel.setText(String.format("Data Encrypted: %.2f MB", dataUsed));
                
                // Send a heartbeat to the server so it knows user is still active
                if (out != null) {
                    out.println("HEARTBEAT: " + String.format("%.2f", dataUsed));
                    if(out.checkError()) {
                        System.out.println("Server connection lost");
                        stopVpnTunnel(statusLabel, actionBtn);
                    }
                }
            })
    );
    trafficTimer.setCycleCount(javafx.animation.Animation.INDEFINITE);
    trafficTimer.play();
    }

    private void stopVpnTunnel(Label statusLabel, Button actionBtn) {
        if (trafficTimer != null) trafficTimer.stop();
    
        try {
            if (out != null) out.println("QUIT");
            if (vpnSocket != null) vpnSocket.close();
            
            // Reset references
            out = null;
            in = null;
            vpnSocket = null;

            statusLabel.setText("Status: Disconnected");
            statusLabel.setStyle("-fx-text-fill: gray;");
            actionBtn.setText("Start VPN");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void show(Stage stage, String username) {
    Label welcomeLabel = new Label("Welcome, " + username);
    welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Label statusLabel = new Label("Status: Disconnected");
    statusLabel.setStyle("-fx-font-size: 14px;");

    Label dataLabel = new Label("Data Encrypted: 0.00 MB");
    dataLabel.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #27ae60;");

    Button actionBtn = new Button("Start VPN");
    actionBtn.setMinWidth(120);

    actionBtn.setOnAction(e -> {
        if (actionBtn.getText().equals("Start VPN")) {
            startVpnTunnel(statusLabel, actionBtn, dataLabel);
        } else {
            stopVpnTunnel(statusLabel, actionBtn);
        }
    });

    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(welcomeLabel, statusLabel, dataLabel, actionBtn);

    Scene scene = new Scene(layout, 400, 300);
    stage.setTitle("OfficeVPN Dashboard");
    stage.setScene(scene);
    stage.show();
}
}
