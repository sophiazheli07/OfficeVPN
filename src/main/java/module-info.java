module OfficeVPN {
    requires javafx.controls;
    requires javafx.fxml;
    // Add 'transitive' here to fix the Stage visibility error
    requires transitive javafx.graphics; 
    requires java.sql;

    exports com.vpn.client;
    exports com.vpn.server;
    
    opens com.vpn.client to javafx.graphics;
}