package finquest.finquest.controller;

import finquest.finquest.Util.Regex;
import finquest.finquest.model.UserTM;
import finquest.finquest.model.tm.FinancialGoalTM;
import finquest.finquest.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private BorderPane mainPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;


    @FXML
    void navigateToDashboard(ActionEvent event) {
        if (isValied()) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if username and password are correct (example logic)
        try {
            if (UserRepository.isValidLogin(username, password)){
                navigateToTheDashboard();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
     }
    @FXML
    void signUpAction(ActionEvent event) {

            BorderPane rootNode = null;
            try {
                rootNode = FXMLLoader.load(this.getClass().getResource("/view/registerForm.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Scene scene = new Scene(rootNode);
            Stage stage = (Stage) this.mainPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign Up ");

            stage.show();
        }

    private void navigateToTheDashboard()  {
        try {
            BorderPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));
            Scene scene = new Scene(rootNode);

            Stage stage = (Stage) this.mainPane.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Finquest ");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,usernameField)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.PASSWORD,passwordField)) return false;
        return true;
    }

    @FXML
    void PasswordAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.PASSWORD,passwordField);
    }

    @FXML
    void UsernameAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,usernameField);
    }

}