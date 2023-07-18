module com.example.bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires mysql.connector.j;

    opens com.example.bank to javafx.fxml;
    exports com.example.bank;
}