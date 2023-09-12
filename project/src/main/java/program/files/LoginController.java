package program.files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label statusLabel;
    public void login(ActionEvent e) throws IOException {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        // check if user has inputted a value into both fields
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter in the remaining field(s)");
        }
        else {

            statusLabel.setText("FAILURE");
            DatabaseConnection db = new DatabaseConnection(username, password);


            if (db.getStatus()) {
                statusLabel.setText("SUCCESS");

                // now load admin GUI
                SunLabAccessAdmin app = new SunLabAccessAdmin();
                app.start(new Stage());

            } else statusLabel.setText("FAILURE");

        }

    }

}
