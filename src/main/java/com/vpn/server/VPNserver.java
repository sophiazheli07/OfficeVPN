package com.vpn.server;

import java.io.*;
import java.net.*;

public class VPNserver {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("VPN Server started on port " + port);

            while (true) {
                // waiting for new client
                Socket clientSocket = serverSocket.accept();
                // endpoint communication established between client and server
                System.out.println("New tunnel request from: " + clientSocket.getInetAddress());

                //starting new thread for each new client 
                new Thread(new ClientHandler(clientSocket)).start();
                // allows the server to handle multiple clients concurrently without blocking the main thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

