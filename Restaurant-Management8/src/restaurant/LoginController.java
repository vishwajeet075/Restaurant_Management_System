package restaurant;
import static restaurant.DB.a;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

public class LoginController implements Initializable {

    @FXML
    TextField username;
    @FXML 
    PasswordField password;

    public void login(Event e) throws IOException, SQLException, ClassNotFoundException {


        if (DB.authenticate(username.getText(),  password.getText())) {
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Menu");

        } else {
            new Alert(Alert.AlertType.ERROR, "Login failed!", ButtonType.OK).show();
        }

    }

    public void exit(Event e) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void register(Event a) throws IOException, SQLException, ClassNotFoundException{
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) a.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Register");
    }
}
