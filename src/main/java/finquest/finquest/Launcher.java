package finquest.finquest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        try {
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/login.fxml"))));
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        stage.setTitle("Finquest Login");
        stage.show();
    }
}
