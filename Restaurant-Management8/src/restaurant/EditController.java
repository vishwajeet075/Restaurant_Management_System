/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurant;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;

/**
 * FXML Controller class
 *
 * @author anas
 */
public class EditController implements Initializable {

    @FXML
    TextField Eid;
    @FXML
    TextField Ename;
    @FXML
    ComboBox<String> Etype;
    @FXML
    TextField Ecost;



    private float getCostFromDatabase(String itemName) {
        float cost = 0.0f;

        try {
            // Get a connection to your database
            Connection conn = DB.getDBConnection();

            // Prepare a SQL query to fetch the cost of the item
            String query = "SELECT cost FROM food WHERE item = ?";
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the item name in the prepared statement
            statement.setString(1, itemName);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // If the result set is not empty, get the cost
            if (resultSet.next()) {
                cost = resultSet.getFloat("cost");
            }

            // Close the resources
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cost;
    }


    public void onEdit(Event e) throws SQLException, IOException {
        int id = Integer.parseInt(Eid.getText());
        Float quantity = Float.valueOf(Ename.getText());
        String type = Etype.getSelectionModel().getSelectedItem().toString();
        float cost = Float.valueOf(Ecost.getText());

        if (MainMenuController.EditTable.equals("_Edit Drinks")) {

            DB.Update("drinks", id, quantity,type, cost);
        } else {
            DB.Update("meals", id, quantity, type, cost);

        }

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();

        Parent root2 = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.centerOnScreen();
        stage2.setTitle("Menu");
        if (MainMenuController.EditTable.equals("_Edit Drinks")) {
            stage2.show();
            MainMenuController.m.setVisible(false);
            MainMenuController.d.setVisible(true);
        } else {
            stage2.show();
            MainMenuController.m.setVisible(true);
            MainMenuController.d.setVisible(false);

        }
        MainMenuController.close();

    }

    public void onCancel(Event e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        int id = MainMenuController.E_id_selection;

        Eid.setText(String.valueOf(id));
        Ename.setText(MainMenuController.E_name_selection);
        Etype.setValue(MainMenuController.E_type_selection);
        Ecost.setText(String.valueOf(MainMenuController.E_cost_selection));
        if (MainMenuController.EditTable.equals("_Edit Drinks")) {
            Etype.getItems().addAll("cola", "Hot-Drink", "coktel");
        } else {
            Etype.getItems().addAll("Burger", "Pizza", "Shawerma");
        }

        Etype.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // Fetch the cost of the selected item from your database
            float cost = getCostFromDatabase(newValue);

            // Update the cost field
            Ecost.setText(String.valueOf(cost));
        });

    }

}
