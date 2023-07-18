package com.example.bank;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button button;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;



    public static Stage stage;
    @FXML
    void btn_login(ActionEvent event) {
        if(login.getText().toString().equals("nazarov") && password.getText().toString().equals("nazarov")) {
            DataBase.preparedStatement = null;
            DataBase.result = null;
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                DataBase.connection = DataBase.getConnection();
                String sql = "SELECT * FROM admin WHERE login = ? and password = ?";
                DataBase.preparedStatement = DataBase.connection.prepareStatement(sql);
                DataBase.preparedStatement.setString(1, DataBase.databaseUser);
                DataBase.preparedStatement.setString(2, DataBase.databasePassword);
                DataBase.result = DataBase.preparedStatement.executeQuery();
                if(DataBase.result.next()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Вхід виконано");
                    alert.showAndWait();
                    Node node = (Node)event.getSource();
                    this.stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("menu.fxml")));
                    stage.setScene(scene);
                    stage.requestFocus();
                    stage.setTitle("Bank Nazarov");
                    stage.show();
                }
            }
            catch(Exception e)
            {e.printStackTrace();}
            finally {
                if (DataBase.result != null) {
                    try {
                        DataBase.result.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (  DataBase.preparedStatement != null) {
                    try {
                        DataBase.preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(login.getText().isEmpty() && password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Введіть логін та пароль");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Невірний логін або пароль");
            alert.showAndWait();
        }
    }
}
