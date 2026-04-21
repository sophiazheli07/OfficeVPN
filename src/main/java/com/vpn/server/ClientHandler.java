package com.vpn.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            
            String input;
            while ((input = in.readLine()) != null) {
                if (input.equals("QUIT")) break;

                // Simulate Encryption by encoding to Base64
                String encryptedData = java.util.Base64.getEncoder().encodeToString(input.getBytes());
                
                System.out.println("[TUNNEL] Original: " + input);
                System.out.println("[TUNNEL] Encrypted: " + encryptedData);
                
                out.println("ACK: Data Encrypted");
            }

        } catch (IOException e) {
            System.out.println("User lost connection");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
