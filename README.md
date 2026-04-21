# OfficeVPN - java full-stack VPN simulation

A cross-platform VPN client-server application built with Java 25, JavaFX, and MySQL. This project demonstrates secure user authentication, multithreaded networking, and real-time data tunneling.

## Features
- secure Login: validates credentials against a MySQL database using JDBC(Java Database Connectivity).
- multithreaded server: Handles multiple concurrent client sessions using `Socket` and `Thread` pooling.
- real-time Dashboard: JavaFX-based UI showing live "data encryption" status and connection heartbeats.
- data tunneling: Simulates data obfuscation using Base64 encoding at the server level.

## Tech Stack
- Language: Java 25
- GUI: JavaFX 21
- Database: MySQL 8.0+
- Build Tool: Maven

## How to Run

### 1. Database setup
execute the following SQL in MySQL Workbench to prepare the backend:
```sql
CREATE DATABASE office_vpn;
USE office_vpn;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
INSERT INTO users (username, password) VALUES ('admin', '12345');
```
## 2. start the vpn server 
mvn compile
java -cp target/classes com.vpn.server.VPNServer

## launch the client
mvn javafx:run

## project structure

1.encapsulation: i separated the `DatabaseManager` from the UI to follow the Single Responsibility Principle.
2.concurrency: i used JavaFX `Task` to keep the UI responsive while performing network I/O in the background.
3.persistence: the socket connection stays open after the handshake to allow for continuous monitoring of activity.
