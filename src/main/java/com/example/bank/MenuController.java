package com.example.bank;

import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button logout;

    @FXML
    private TableColumn<User, String> addressColm;

    @FXML
    private TextField addressField;

    @FXML
    private TableColumn<User, BigDecimal> balanceColm;

    @FXML
    private TableColumn<User, Integer> idColm;

    @FXML
    private TableColumn<User, Integer> indexColm;

    @FXML
    private TextField indexField;

    @FXML
    private TableColumn<User, String> nameColm;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numClient;

    @FXML
    private TextField purposeField;

    @FXML
    private TextField startBalance;

    @FXML
    private TableColumn<User, String> secondnameColm;

    @FXML
    private TextField secondnameField;

    @FXML
    private TextField sumField;

    @FXML
    private TableColumn<User, Date> dateColm;

    @FXML
    private TableView<User> table;

    public static Stage stage;

    ObservableList<User> userObservableList = FXCollections.observableArrayList();


    public static int indexOfHistory;
    public static String balanceOfHistory;



    @FXML
    void addClient(ActionEvent event) {
        DataBase.preparedStatement = null;
        DataBase.result = null;
        try {
            String sql = "insert into bank(name, secondname, address, postindex,regDate, balance) values (?, ?, ?, ?, ?, ?)";
            DataBase.preparedStatement = DataBase.connection.prepareStatement(sql);

            DataBase.preparedStatement.setString(1, nameField.getText());
            DataBase.preparedStatement.setString(2, secondnameField.getText());
            DataBase.preparedStatement.setString(3,addressField.getText());
            DataBase.preparedStatement.setInt(4, Integer.parseInt(indexField.getText()));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            DataBase.preparedStatement.setString(5, dtf.format(now));

            if(startBalance.getText().isEmpty())  DataBase.preparedStatement.setBigDecimal(6,new BigDecimal(0));
               else {
                String startBalanceText = startBalance.getText().replace(",", ".");
                DataBase.preparedStatement.setBigDecimal(6, new BigDecimal(startBalanceText));
            }

            DataBase.preparedStatement.executeUpdate();
            clearFieldAdd();
            displayInfo();


        } catch (SQLException e) {
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
    @FXML
    void history(ActionEvent event) throws IOException {
        try {
            if (table.getSelectionModel() == null || table.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Оберіть користувача з таблиці!");
                    alert.showAndWait();
                }
                else {

                        indexOfHistory = table.getSelectionModel().getSelectedItem().getId();
                        balanceOfHistory = table.getSelectionModel().getSelectedItem().getBalance();
                        Stage stageNew = new Stage();
                        stageNew.initOwner(LoginController.stage);
                        stageNew.setResizable(false);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("history.fxml"));
                        Parent root = loader.load();
                        HistoryController newStageController = loader.getController();
                        newStageController.setData(indexOfHistory, balanceOfHistory);
                        stageNew.setScene(new Scene(root));
                        root.requestFocus();
                        stageNew.show();
                    }
                }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        }
    void clearFieldAdd(){
        nameField.clear();
        secondnameField.clear();
        addressField.clear();
        indexField.clear();
        startBalance.clear();
    }

    @FXML
    void logOut(ActionEvent event) {
        try {
            if(event.getSource() == logout) {
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

                Stage stage = new Stage();
                stage.setResizable(false);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                scene.getRoot().requestFocus();
                stage.show();

                logout.getScene().getWindow().hide();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void transaction(ActionEvent event) {
        DataBase.preparedStatement = null;
        DataBase.result = null;
        try{
            if(containsId(table,Integer.parseInt(numClient.getText()))){
                // updating main table
                String updateBalance = "UPDATE bank SET balance = balance + ? WHERE id = ?";
                DataBase.preparedStatement = DataBase.connection.prepareStatement(updateBalance);
                BigDecimal amount = new BigDecimal(sumField.getText());
                DataBase.preparedStatement.setBigDecimal(1, amount);
                DataBase.preparedStatement.setString(2, numClient.getText());
               DataBase.preparedStatement.executeUpdate();
                displayInfo();

                String getUpdatedBalance = "SELECT balance FROM bank WHERE id = ?";
                DataBase.preparedStatement = DataBase.connection.prepareStatement(getUpdatedBalance);
                DataBase.preparedStatement.setString(1, numClient.getText());
                ResultSet balanceResult = DataBase.preparedStatement.executeQuery();
                BigDecimal updatedBalance = BigDecimal.ZERO;
                if (balanceResult.next()) {
                    updatedBalance = balanceResult.getBigDecimal("balance");
                }

                // inserting into transaction
                String query = "insert into transaction(idClient,purpose,sum,date,balance) values(?,?,?,?,?)";
                DataBase.preparedStatement = DataBase.connection.prepareStatement(query);
                DataBase.preparedStatement.setInt(1, Integer.parseInt(numClient.getText()));
                DataBase.preparedStatement.setString(2,purposeField.getText());
                DataBase.preparedStatement.setBigDecimal(3,amount);

                java.util.Date currentDate = new java.util.Date();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentDate.getTime());
                DataBase.preparedStatement.setTimestamp(4, currentTimestamp);


                DataBase.preparedStatement.setBigDecimal(5 ,updatedBalance);

                DataBase.preparedStatement.executeUpdate();

                numClient.clear();
                purposeField.clear();
                sumField.clear();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Клієнта неіснує");
                alert.showAndWait();
            }
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
    public boolean containsId(TableView<User> tableView, int id) {
        for (User item : tableView.getItems()) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

//    @FXML
//    void excel(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Excel File");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
//
//        // Show the dialog and get the selected file
//        File file = fileChooser.showSaveDialog(new Stage());
//        if (file != null) {
//            try (Workbook workbook = new XSSFWorkbook()) {
//                // Create a new sheet in the workbook
//                Sheet sheet = workbook.createSheet("Data");
//
//                // Get the data from the TableView
//                ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();
//
//                // Write the column headers to the first row
//                Row headerRow = sheet.createRow(0);
//                for (int columnIndex = 0; columnIndex < table.getColumns().size(); columnIndex++) {
//                    TableColumn<?, ?> column = table.getColumns().get(columnIndex);
//                    Cell headerCell = headerRow.createCell(columnIndex);
//                    headerCell.setCellValue(column.getText());
//                }
//
//                // Write the table data to subsequent rows
//                for (TablePosition<?, ?> position : selectedCells) {
//                    int rowIndex = position.getRow();
//                    Row row = sheet.createRow(rowIndex + 1);
//
//                    for (int columnIndex = 0; columnIndex < table.getColumns().size(); columnIndex++) {
//                        TableColumn<?, ?> column = table.getColumns().get(columnIndex);
//                        Cell cell = row.createCell(columnIndex);
//                        cell.setCellValue(column.getCellData(rowIndex).toString());
//                    }
//                }
//
//                // Write the workbook to the file
//                try (FileOutputStream fos = new FileOutputStream(file)) {
//                    workbook.write(fos);
//                }
//
//                System.out.println("Export completed successfully.");
//            } catch (IOException | NoSuchFieldError e) {
//                e.printStackTrace();
//            }
//        }
//    }

    void displayInfo(){
        userObservableList.clear();
        DataBase.preparedStatement = null;
        DataBase.result = null;
        try{
            String sql = "SELECT * FROM bank";

            DataBase.preparedStatement = DataBase.connection.prepareStatement(sql);
            DataBase.result = DataBase.preparedStatement.executeQuery();

            while (DataBase.result.next()){


                BigDecimal balance = DataBase.result.getBigDecimal("balance");
                String formattedBalance = "$" + balance.toString();

                userObservableList.add(new User(
                        DataBase.result.getInt("id"),
                       DataBase.result.getString("name"),
                       DataBase.result.getString("secondname"),
                       DataBase.result.getString("address"),
                       DataBase.result.getInt("postIndex"),
                       DataBase.result.getDate("regDate"),
                        formattedBalance
                ));
                table.setItems(userObservableList);
            }
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

    @FXML
    void deleteUser(ActionEvent event) {
        User selectedPerson = table.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            int id = selectedPerson.getId();

            table.getItems().remove(selectedPerson);

            DataBase.preparedStatement = null;
            DataBase.result = null;

            try {
                String sql = "DELETE FROM bank.bank WHERE id = ?";
                DataBase.preparedStatement = DataBase.connection.prepareStatement(sql);
                DataBase.preparedStatement.setInt(1, id);
                DataBase.preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (DataBase.result != null) {
                    try {
                        DataBase.result.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (DataBase.preparedStatement != null) {
                    try {
                        DataBase.preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Оберіть клієнта для видалення");
            alert.showAndWait();
        }
    }
    private void setCellTable(){
        idColm.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColm.setCellValueFactory(new PropertyValueFactory<>("name"));
        secondnameColm.setCellValueFactory(new PropertyValueFactory<>("secondname"));
       addressColm.setCellValueFactory(new PropertyValueFactory<>("address"));
       indexColm.setCellValueFactory(new PropertyValueFactory<>("postIndex"));
       dateColm.setCellValueFactory(new PropertyValueFactory<>("date"));
       balanceColm.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellTable();
        displayInfo();
    }
}
