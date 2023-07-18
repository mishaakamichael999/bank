package project.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class DataBase {
    static final String databaseName = "library";
    static final String databaseUser = "admin";
    static final String databasePassword = "admin";
    static final String url = "jdbc:mysql://localhost/" + databaseName;
    public static PreparedStatement preparedStatement;
    public static ResultSet result;
    public static Connection connection;

    public static Connection getConnection() throws SQLException{
        connection = DriverManager.getConnection(url, databaseUser, databasePassword);
        return connection;
    }

}
