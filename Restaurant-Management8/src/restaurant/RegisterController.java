package restaurant;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void Register(Event e) throws IOException, SQLException, ClassNotFoundException {

        if (DB.register(username.getText(),password.getText())) {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Login");

        } else {
            new Alert(Alert.AlertType.ERROR, "Registration failed!", ButtonType.OK).show();
        }
    }

    public void exit(Event e) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    }

