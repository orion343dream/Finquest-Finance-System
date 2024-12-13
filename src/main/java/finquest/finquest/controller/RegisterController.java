package finquest.finquest.controller;
import finquest.finquest.Util.Regex;
import finquest.finquest.db.DbConnection;
import finquest.finquest.model.User;
import finquest.finquest.repository.UserRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterController {
    @FXML
    private BorderPane mainPane;


    @FXML
    private PasswordField txtConfirmPassword;


    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtUserName;

    @FXML
    void handleRegisterButton(ActionEvent event) {
        if (isValied()) {
            String username = txtUserName.getText();
            String password = txtPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();
            String userId = txtUserId.getText();


            if (!password.equals(confirmPassword)) {
                new Alert(Alert.AlertType.CONFIRMATION, "password and confirm Password does not match !!").show();
                return;
            }

            User user = new User(userId, username, password);
            // Validate input fields
            try {
                boolean isSaved = UserRepository.saveUser(user);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User saved!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    @FXML
    void navigateToLogin(ActionEvent event) throws IOException {
        BorderPane  rootNode = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/login.fxml")));


        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.mainPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login ");

        stage.show();
    }

    public boolean isValied() {
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtUserName)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtPassword)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtConfirmPassword)) return false;
        if (!Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtUserId)) return false;
        return true;
    }

    @FXML
    void enterpasswordAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.PASSWORD,txtPassword);
    }

    @FXML
    void enterUnameAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtUserName);
    }
    @FXML
    void enterconfrimpAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.PASSWORD,txtConfirmPassword);
    }
    @FXML
    void enterUidAction(KeyEvent event) {
        Regex.setTextColor(finquest.finquest.Util.TextField.ID,txtUserId);
    }

}