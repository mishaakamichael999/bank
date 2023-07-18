package com.example.bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private Label balanceHistory;

    @FXML
    private TableColumn<Transaction, BigDecimal> balanceColm;

    @FXML
    private TableColumn<Transaction, Date> dateColm;

    @FXML
    private TableColumn<Transaction, Integer> idColm;

    @FXML
    private Label idHistory;

    @FXML
    private TableColumn<Transaction, String> purposeColm;

    @FXML
    private TableColumn<Transaction, BigDecimal> sumColm;

    @FXML
    private TableView<Transaction> table;

    ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList();

    void setData(int index, String balance){
        idHistory.setText(String.valueOf(index));
        balanceHistory.setText(balance.toString());
    }

    void selectHistory(){
        transactionObservableList.clear();
        DataBase.preparedStatement = null;
        DataBase.result = null;
        try {
            String sql = "select * from transaction where idClient = ?";
            DataBase.preparedStatement = DataBase.connection.prepareStatement(sql);

            DataBase.preparedStatement.setInt(1,MenuController.indexOfHistory);
            DataBase.result = DataBase.preparedStatement.executeQuery();
            while (DataBase.result.next()) {
                transactionObservableList.add(new Transaction(
                        DataBase.result.getInt("idTrans"),
                        DataBase.result.getString("purpose"),
                DataBase.result.getBigDecimal("sum"),
                DataBase.result.getDate("date"),
                DataBase.result.getBigDecimal("balance")
                ));
                table.setItems(transactionObservableList);

            }

            sumColm.setCellFactory(column -> {
                return new TableCell<Transaction, BigDecimal>() {

                    protected void updateItem(BigDecimal item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item.toString());

                            // Check if the sum contains a "+"
                            if (item.compareTo(BigDecimal.ZERO) > 0) {
                                setStyle("-fx-background-color: green;");
                            } else {
                                setStyle("-fx-background-color: red;");

                            }
                        }
                    }
                };
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
    private void setCellTable(){
        idColm.setCellValueFactory(new PropertyValueFactory<>("idTrans"));
        purposeColm.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        sumColm.setCellValueFactory(new PropertyValueFactory<>("sum"));
        dateColm.setCellValueFactory(new PropertyValueFactory<>("date"));
        balanceColm.setCellValueFactory(new PropertyValueFactory<>("balance"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectHistory();
        setCellTable();
    }
}
