package restaurant;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Objects;

public class BillController {
    @FXML
    private TableView<BillItem> customTable;

    @FXML
    private TableColumn<BillItem, String> itemColumn;
    @FXML
    private TableColumn<BillItem, Integer> quantityColumn;
    @FXML
    private TableColumn<BillItem, Float> priceColumn;
    @FXML
    private TableColumn<BillItem, Float> totalColumn;

    @FXML
    private ComboBox<String> customOptions;

    @FXML
    private Label customLabel;

    @FXML
    private ImageView customImage;

    private ObservableList<BillItem> billItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        float gstRate = 0.18f; // GST rate of 18%. Change this to your actual rate.

        totalColumn.setCellValueFactory(cellData -> {
            float price = cellData.getValue().getPrice();
            float priceWithGST = price + (price * gstRate);
            return new ReadOnlyObjectWrapper<>(priceWithGST);
        });

        customOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (customOptions.getValue().equals("Phone Pay")) {
                Image image = new Image(getClass().getResource("/download.jpg").toExternalForm());

                customImage.setImage(image);
            }
            // Your custom logic based on selected option
        });

        // Load a custom image
        Image image = new Image("file:/path/to/your/image.jpg"); // Change path to your image file
        customImage.setImage(image);

        // Set text for the custom label
        customLabel.setText("Custom Label Text");

        // Populate TableView with data from the database
        populateTable();

        // Calculate and display the total bill cost
        calculateTotal();
    }

    private void populateTable() {
        // Clear previous data
        billItems.clear();

        // Connect to your database and fetch data
        try {
            Connection conn = DB.getDBConnection();
            String query = "SELECT quantity , Meal_Name , Mcost from meals";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();


            // Iterate through the result set and add items to the list
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                String mealName = resultSet.getString("Meal_Name");
                float cost = resultSet.getFloat("Mcost");

                BillItem item = new BillItem(mealName, quantity, cost);
                billItems.add(item);
            }

            String query1 = "SELECT quantityD , D_Name , Dcost from drinks";
            PreparedStatement statement1 = conn.prepareStatement(query1);
            ResultSet resultSet1 = statement1.executeQuery();


            // Iterate through the result set and add items to the list
            while (resultSet1.next()) {
                int quantity1 = resultSet1.getInt("quantityD");
                String drinkName = resultSet1.getString("D_Name");
                float cost1 = resultSet1.getFloat("Dcost");

                BillItem item1 = new BillItem(drinkName, quantity1, cost1);
                billItems.add(item1);
            }


            // Close resources
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the items in the table
        customTable.setItems(billItems);
    }

    private void calculateTotal() throws SQLException {
        float total = 0;
        float gstRate = 0.18f; // GST rate of 18%. Change this to your actual rate.

        for (BillItem item : billItems) {
            total +=  item.getPrice();
        }

        // Add GST to the total
        total += total * gstRate;

        customLabel.setText("Total Bill: " + String.format("%.2f", total));

        Connection conn = DB.getDBConnection();
        PreparedStatement ps = conn.prepareStatement("insert into customer (customer_id , order_amount,check_out_time) values(?,?,?)");


        int customerID=CustomerSession.getCustomerId();
        ps.setInt(1,customerID);
        ps.setFloat(2,total);
        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        int rowsAffected = ps.executeUpdate();

        if(rowsAffected>0){
            System.out.println("succesfull");
        }
        else{
            System.out.println("unsuccesfull");
        }
        try {

            String query = "TRUNCATE TABLE meals";
            String query1 = "TRUNCATE TABLE drinks";
            PreparedStatement statement = conn.prepareStatement(query);
            PreparedStatement statement1 = conn.prepareStatement(query1);
            statement.execute();
            statement1.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logout(Event e)  throws  Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);

        stage.show();
    }
}
