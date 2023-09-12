package program.files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Controller {

    @FXML
    public void manageButtonAction() {

    }

    @FXML
    private CheckBox idCheckBox;
    @FXML
    private CheckBox dateCheckBox;
    @FXML
    private CheckBox timeCheckBox;
    @FXML
    private TextField idSearchTextField;
    @FXML
    private DatePicker dateTextField;
    @FXML
    private ComboBox<String> timeComboBox;
    @FXML
    private TableView<AccessLogRow> resultsTableView;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn timeColumn;
    @FXML
    Label filterStatusLabel;

    public void searchButtonAction(ActionEvent e) throws SQLException {

        // first need to process date
        String date = "";
        if (dateCheckBox.isSelected()) {
            if (dateTextField.getValue() == null) {
                filterStatusLabel.setText("Please enter a date");
                return;
            }
            else {
                date = " time like '%" + dateTextField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%'";
            }
        }
        // now need to process time input
        String time = "";
        String range = timeComboBox.getSelectionModel().getSelectedItem();

        if (range.equals("Early Morning")) {
            time = " hour(time) BETWEEN 0 AND 4";
        }
        else if (range.equals("Morning")) {
            time = " hour(time) BETWEEN 5 AND 11";
        }
        else if (range.equals("Afternoon")) {
            time = " hour(time) BETWEEN 12 AND 20";
        }
        else if (range.equals("Night")) {
            time = " hour(time) >= 20";
        }

        String query = "SELECT * FROM access_log WHERE ";
        String[] constraints = { "", "", ""};

        // query constraints
        if (idCheckBox.isSelected()) {
            // checking for input in id text field
            if (idSearchTextField.getText().isEmpty()) {
                filterStatusLabel.setText("Please enter an id");
                return;
            }
            else {
                constraints[0] = " id = " + idSearchTextField.getText();
            }
        }
        if (dateCheckBox.isSelected()) {
            constraints[1] = date;
        }
        if (timeCheckBox.isSelected()){
            constraints[2] = time;
        }
        // if no check box is selected, do not go through with query
        if (!idCheckBox.isSelected() && !dateCheckBox.isSelected() && !timeCheckBox.isSelected()) {
            filterStatusLabel.setText("Please select a filter");
            return;
        }


        // merge constraints into query
        boolean concatConstraint = false;
        for (int i = 0; i < constraints.length; i++) {
            if (!constraints[i].isEmpty() && !concatConstraint) {
                query += constraints[i];
                concatConstraint = true;
            }
            else if (!constraints[i].isEmpty() && concatConstraint) {
                query += " AND " + constraints[i];
            }
        }

        ArrayList<AccessLogRow> rows = DatabaseConnection.queryAccess_log(query);

        // make sure tableView is cleared first
        resultsTableView.getItems().clear();

        // set cell factory value for tableview columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // display results of query in results tab
        for (AccessLogRow row : rows) {
            resultsTableView.getItems().add(row);
        }

        filterStatusLabel.setText("SUCCESS");

    }

    @FXML
    RadioButton activateRadioButton;
    @FXML
    RadioButton disableRadioButton;
    @FXML
    RadioButton reactivateRadioButton;
    @FXML
    TextField idManageTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField positionTextField;
    @FXML
    Label successLabel;

    public void applyButtonAction(ActionEvent e) {

        if (activateRadioButton.isSelected()) {
            // ensure user has inputted values into all required fields
            if (idManageTextField.getText().isEmpty() || nameTextField.getText().isEmpty()
                    || positionTextField.getText().isEmpty()) {
                successLabel.setText("Please enter in the required fields");
                return;
            }
            if (!DatabaseConnection.isValidID(idManageTextField.getText())) {
                successLabel.setText("Please enter in a valid id of digits with length 9");
                return;
            }
            DatabaseConnection.addUser(idManageTextField.getText(), nameTextField.getText(), positionTextField.getText());
            successLabel.setText("SUCCESS");
        }
        else if (disableRadioButton.isSelected() || reactivateRadioButton.isSelected()) {
            if (!DatabaseConnection.isValidID(idManageTextField.getText())) {
                successLabel.setText("Please enter in a valid id");
                return;
            }
            boolean idFound = DatabaseConnection.changeUserStatus(idManageTextField.getText(), reactivateRadioButton.isSelected());
            if (idFound) successLabel.setText("SUCCESS");
            else successLabel.setText("id not found");
        }
        else {
            successLabel.setText("Please select an option");
        }

    }

    @FXML
    HBox addingUserContainer;

    public void displayAddingUserFields(ActionEvent e) {

        addingUserContainer.setVisible(activateRadioButton.isSelected());

    }

}