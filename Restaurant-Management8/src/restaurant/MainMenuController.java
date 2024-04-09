package restaurant;
import javafx.event.ActionEvent;
import restaurant.DB;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static restaurant.DB.a;

/**
 * FXML Controller class
 *
 * @author anas
 */
public class MainMenuController implements Initializable {

    @FXML
    TextField numMeals;
    @FXML
    TextField numDrinks;
    @FXML
    Pane Meals;
    @FXML
    Pane Drinks;
    @FXML
    ComboBox typeD;
    @FXML
    ComboBox typeM;
    @FXML
    TextField numM;
    @FXML
    TextField quantityM;
    @FXML
    Label lblnameM;
    @FXML
    TextField costM;
    @FXML
    Label lblcostM;
    @FXML
    Label lblTypeM;
    @FXML
    TextField numD;
    @FXML
    TextField quantityD;
    @FXML
    Label lblnameD;
    @FXML
    Label lblTypeD;
    @FXML
    TextField costD;
    @FXML
    Label lblcostD;
    //tableview m:
    ObservableList<Meals> listM = null;
    @FXML
    TableView<Meals> tableM;
    @FXML
    TableColumn<Meals, Integer> numCM;
    @FXML
    TableColumn<restaurant.Meals, Float> nameCM;
    @FXML
    TableColumn<Meals, String> typeCM;
    @FXML
    TableColumn<restaurant.Meals, Float> costCM;
    //tableview d:
    ObservableList<Drinks> listD = null;
    @FXML
    TableView<Drinks> tableD;
    @FXML
    TableColumn<Drinks, Integer> numCD;
    @FXML
    TableColumn<restaurant.Drinks, Float> nameCD;
    @FXML
    TableColumn<Drinks, String> typeCD;
    @FXML
    TableColumn<restaurant.Drinks, Float> costCD;
    public static String EditTable = "";
    public static int E_id_selection;
    public static String E_name_selection;
    public static String E_type_selection;
    public static double E_cost_selection;

    public static Stage MainStage;
    public static Pane m;
    public static Pane d;

    public void entred(Event e) {
        ((Button) e.getSource()).setScaleX(1.1);
        ((Button) e.getSource()).setScaleY(1.1);
    }

    public void exited(Event e) {
        ((Button) e.getSource()).setScaleX(1);
        ((Button) e.getSource()).setScaleY(1);
    }

    public void Drinks() {
        Meals.setVisible(false);
        Drinks.setVisible(true);

    }

    public void Meals() {
        Meals.setVisible(true);
        Drinks.setVisible(false);

    }

    public void ClearMeals() {
        numM.clear();
        quantityM.clear();
        typeM.getSelectionModel().clearSelection();
        costM.clear();
    }

    public void ClearDrinks() {
        numD.clear();
        quantityD.clear();
        typeD.getSelectionModel().clearSelection();
        costD.clear();
    }

    public void onLogout(Event e) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);

        stage.show();

    }

    public static void close() {
        MainStage.close();
    }

    public void addMeals() throws SQLException {

        float  quantity = (float) Double.parseDouble(quantityM.getText());
        String type = typeM.getSelectionModel().getSelectedItem().toString();


        if (DB.insertMeals( quantity, type)) {

            a.show();
        }

        try {
            numMeals.setText(String.valueOf(DB.count("Mid", "meals")));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int counter = Integer.parseInt(numMeals.getText());
        numM.setText(String.valueOf(counter));

        try {
            listM = DB.getMeals();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableM.setItems(listM);
    }

    public void deleteMeals() throws SQLException {
        Meals id = tableM.getSelectionModel().getSelectedItem();

        DB.delMeals(id.getId());
        try {
            listM = DB.getMeals();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableM.setItems(listM);
        try {
            numMeals.setText(String.valueOf(DB.count("Mid", "meals")));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int counter = Integer.parseInt(numMeals.getText());
        numM.setText(String.valueOf(counter));
    }

    public void deleteDrinks() throws SQLException {
        Drinks id = tableD.getSelectionModel().getSelectedItem();

        DB.delDrinks(id.getId());
        try {
            listD = DB.getDrinks();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableD.setItems(listD);
        try {
            numDrinks.setText(String.valueOf(DB.count("Did", "drinks")));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int counter = Integer.parseInt(numDrinks.getText());
        numD.setText(String.valueOf(counter));
    }

    public void addDrinks() throws SQLException {
        String quantityText = quantityD.getText();
        if (!quantityText.isEmpty()) {
            float quantity1 = (float) Double.parseDouble(quantityText);
            String type = (String) typeD.getSelectionModel().getSelectedItem();
            if (type != null) {
                if (DB.insertDrinks(quantity1, type)) {
                    a.show(); // Show alert if insertion is successful
                }
                try {
                    numDrinks.setText(String.valueOf(DB.count("Did", "drinks")));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    listD = DB.getDrinks();
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
                tableD.setItems(listD);
            } else {
                // Handle the case where no type is selected
            }
        } else {
            // Handle the case where quantity text field is empty
        }
    }


    public void update(Event e) throws IOException {
        Stage stage1 = (Stage) ((Node) e.getSource()).getScene().getWindow();
        MainStage = stage1;
        EditTable = ((Button) e.getSource()).getText();
        if (EditTable.equals("_Edit Drinks") && tableD.getSelectionModel().getSelectedItem() != null) {
            E_id_selection = tableD.getSelectionModel().getSelectedItem().getId();
            E_name_selection = String.valueOf(tableD.getSelectionModel().getSelectedItem().getName());
            E_type_selection = tableD.getSelectionModel().getSelectedItem().getType();
            E_cost_selection = tableD.getSelectionModel().getSelectedItem().getCost();
        } else if (tableM.getSelectionModel().getSelectedItem() != null) {
            E_id_selection = tableM.getSelectionModel().getSelectedItem().getId();
            E_name_selection = String.valueOf(tableM.getSelectionModel().getSelectedItem().getName());
            E_type_selection = tableM.getSelectionModel().getSelectedItem().getType();
            E_cost_selection = tableM.getSelectionModel().getSelectedItem().getCost();
        } else {
            // Handle the case where no item is selected, show a message or perform any necessary action
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("Edit.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit");
        stage.show();
    }


    public void loadData() throws SQLException {
        listM = DB.getMeals();
        tableM.setItems(listM);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        m = Meals;
        d = Drinks;




        // For typeD (Drinks dropdown)
        typeD.setOnAction(event -> {
            String selectedDrink = typeD.getSelectionModel().getSelectedItem().toString();
            float cost = 0; // Assuming you have a method in your DB class to retrieve the cost of the selected drink
            try {
                cost = DB.getDrinkCost(selectedDrink);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            costD.setText(String.valueOf(cost));
        });

        // For typeM (Meals dropdown)
        typeM.setOnAction(event -> {
            String selectedMeal = typeM.getSelectionModel().getSelectedItem().toString();
            float cost = 0; // Assuming you have a method in your DB class to retrieve the cost of the selected meal
            try {
                cost = DB.getMealCost(selectedMeal);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            costM.setText(String.valueOf(cost));
        });




        //Meals: , Drinks:
        try {
            numMeals.setText(String.valueOf(DB.count("Mid", "meals")));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            numDrinks.setText(String.valueOf(DB.count("Did", "drinks")));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //cbo
        typeD.getItems().addAll("cola", "Hot-Drink", "coktel");
        typeM.getItems().addAll("Burger", "Pizza", "Shawerma");


        // Assuming typeD and typeM are ComboBoxes in your JavaFX application

// For typeD (Drinks dropdown)
        ObservableList<String> drinkItems = FXCollections.observableArrayList();
        try {
            Connection conn = DB.getDBConnection(); // Assuming DB.getConnection() gets a database connection
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT  drink_name FROM Beverages"); // Adjust the query based on your table structure
            while (rs.next()) {
                drinkItems.add(rs.getString("drink_name"));
            }
            typeD.setItems(drinkItems);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }

// For typeM (Meals dropdown)
        ObservableList<String> mealItems = FXCollections.observableArrayList();
        try {
            Connection conn = DB.getDBConnection(); // Assuming DB.getConnection() gets a database connection
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT item FROM food"); // Adjust the query based on your table structure
            while (rs.next()) {
                mealItems.add(rs.getString("item"));
            }
            typeM.setItems(mealItems);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }




        lblnameD.setLabelFor(quantityD);
        lblTypeD.setLabelFor(typeD);
        lblcostD.setLabelFor(costD);

        lblnameM.setLabelFor(quantityM);
        lblTypeM.setLabelFor(typeM);
        lblcostM.setLabelFor(costM);

        numCM.setCellValueFactory(new PropertyValueFactory<Meals, Integer>("id"));
        nameCM.setCellValueFactory(new PropertyValueFactory<Meals, Float>("name"));
        typeCM.setCellValueFactory(new PropertyValueFactory<Meals, String>("type"));
        costCM.setCellValueFactory(new PropertyValueFactory<Meals, Float>("cost"));

        numCD.setCellValueFactory(new PropertyValueFactory<Drinks, Integer>("id"));
        nameCD.setCellValueFactory(new PropertyValueFactory<Drinks, Float>("name"));
        typeCD.setCellValueFactory(new PropertyValueFactory<Drinks, String>("type"));
        costCD.setCellValueFactory(new PropertyValueFactory<Drinks, Float>("cost"));

        try {
            listM = DB.getMeals();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableM.setItems(listM);

        try {
            listD = DB.getDrinks();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableD.setItems(listD);

    }

    public void placeorder(Event e) {
        try {
            Parent billParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Bill.fxml")));
            Scene billScene = new Scene(billParent);

            // This line gets the stage information
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            window.setScene(billScene);
            window.show();
        } catch (IOException e1) {
            e1.printStackTrace(); // Handle the exception properly
        }
    }
}
