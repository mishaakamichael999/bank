package com.example.bank;

import java.sql.*;
public class DataBase {
    static final String databaseName = "bank";
    static final String databaseUser = "nazarov";
    static final String databasePassword = "nazarov";
    static final String url = "jdbc:mysql://localhost/" + databaseName;
    public static PreparedStatement preparedStatement;
    public static ResultSet result;
    public static Connection connection;

    public static Connection getConnection() throws SQLException{
        connection = DriverManager.getConnection(url, databaseUser, databasePassword);
        return connection;
    }
}
