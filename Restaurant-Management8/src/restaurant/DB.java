package restaurant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DB {

    public static Alert a = new Alert(Alert.AlertType.INFORMATION, "Succesful Add", ButtonType.OK);
    public static Alert E = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);

    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Restaurant";
            String user = "vishwa075";
            String password = "shinde@200380";
            con = DriverManager.getConnection(url, user, password);
            //JOptionPane.showMessageDialog(null, "Done");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Exception: " + ex.getMessage());
            E.show();
        }
        return con;
    }
    public static Connection getDBConnection() throws SQLException {
        return getConnection(); // Call the private getConnection method
    }

    public static int count(String col, String Table) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select count(" + col + ") from " + Table;
        ResultSet result = s.executeQuery(sqlSelect);
        if (result.next()) {
            return Integer.parseInt(result.getString(1));
        }

        return 0;
    }

    public static boolean insertMeals( float quantity , String type) throws SQLException {
        Connection conn=getConnection();
        PreparedStatement ps = conn.prepareStatement("select cost from food where item = ?");
        ps.setString(1,type);
        ResultSet rs=ps.executeQuery();
        float cost1=0;
        if (rs.next()) {
             cost1 = rs.getFloat("cost");
        }
        float final_cost = cost1*quantity;
        try {
            Connection con = getConnection();
            Statement s = con.createStatement();
            String SqlInsert = "INSERT INTO Meals(quantity, Meal_Name, Mcost) VALUES (" + quantity + ", '" + type + "', " + final_cost + ")";



            s.execute(SqlInsert);
            return true;
        } catch (Exception e) {
            E.show();
            return false;
        }
    }

    public static boolean insertDrinks(Float quan, String type) throws SQLException {
        try (Connection conn = getConnection()) {
            // Retrieve the cost of the specified beverage
            PreparedStatement ps = conn.prepareStatement("SELECT cost1 FROM Beverages WHERE drink_name = ?");
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            float cost = 0;
            if (rs.next()) {
                cost = rs.getFloat("cost1");
            }

            // Calculate the final cost based on quantity and unit cost
            float finalCost = cost * quan;


            // Insert the drink order into the database
            String sqlInsert = "INSERT INTO drinks (quantityD, D_Name, Dcost) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setFloat(1, quan);
                pstmt.setString(2, type);
                pstmt.setFloat(3, finalCost);
                int rowsAffected = pstmt.executeUpdate();
              if(rowsAffected>0)
              {
                  System.out.println("The data is properly stored in the database");
                  return  true;
              }
              else {
                  return  false;
              }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            E.show(); // Show an alert if an exception occurs
            return false; // Return false to indicate insertion failure
        }
    }




    public static ObservableList<Meals> getMeals() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select * from meals";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Meals> mealsList = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("Mid");
            Float quantity = result.getFloat("quantity");
            String type = result.getString("Meal_Name");
            float cost = result.getFloat("Mcost");
            mealsList.add(new Meals(id, cost, type, quantity));
        }
        return mealsList;
    }

    public static boolean delMeals(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM meals WHERE Mid = " + id + " ";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }

    public static boolean delDrinks(int id) throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlDelete = "DELETE FROM Drinks WHERE Did = " + id + " ";
        boolean execute = s.execute(sqlDelete);

        return execute;
    }

    public static ObservableList<Drinks> getDrinks() throws SQLException {
        Connection con = getConnection();
        Statement s = con.createStatement();
        String sqlSelect = "select * from drinks";
        ResultSet result = s.executeQuery(sqlSelect);
        ObservableList<Drinks> drinkList = FXCollections.observableArrayList();
        while (result.next()) {
            int id = result.getInt("Did");
            Float name = result.getFloat("quantityD");
            String type = result.getString("D_Name");
            float cost = (float) result.getDouble("Dcost");
            drinkList.add(new Drinks(id, cost, type, name));
        }
        return drinkList;
    }


    public static boolean Update(String table, int id, Float name, String type, float cost) {
        try (Connection con = getConnection()) {
            String sqlSelect;
            if (table.equals("drinks")) {
                sqlSelect = "UPDATE drinks SET quantityD = ?, D_Name = ?, Dcost = ? WHERE Did = ?";
            } else {
                sqlSelect = "UPDATE meals SET quantity = ?, Meal_Name = ?, Mcost = ? WHERE Mid = ?";
            }
            try (PreparedStatement pstmt = con.prepareStatement(sqlSelect)) {
                pstmt.setFloat(1, name);
                pstmt.setString(2, type);
                pstmt.setFloat(3, cost);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
            return false;
        }
    }


    public static boolean authenticate(String username, String password) {
        int customer_id = 0;
        try {
            Connection conn = getConnection();
            String query = "SELECT password FROM users WHERE username = ?";
            String query2 = "SELECT customer_id FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            PreparedStatement statement1 = conn.prepareStatement(query2);
            statement1.setString(1,username);
            ResultSet rs =statement1.executeQuery();
         
            if(rs.next())
            {
               customer_id=rs.getInt("customer_id");
            }
                
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPasswordHash = resultSet.getString("password");
                String passwordHash = hashPassword(password); // Hash the entered password

                // Compare the hashed entered password with the stored hashed password
                CustomerSession.setCustomerId(customer_id);
                return storedPasswordHash.equals(passwordHash);

            } else {
                return false; // User not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Authentication failed due to exception
        }
    }

    private static final String SQL_INSERT_USER = "INSERT INTO users (username, password,customer_id) VALUES (?, ?,?)";
    private static final String SQL_SELECT_USER1 = "SELECT * FROM users WHERE username = ?";
    // Register method
    public static boolean register(String username, String password) {
        try {
              Connection conn = getConnection();
            PreparedStatement selectStatement = conn.prepareStatement(SQL_SELECT_USER1);
            PreparedStatement insertStatement = conn.prepareStatement(SQL_INSERT_USER);
            // Generate a random customer ID
            Random rand = new Random();
            int customerId = rand.nextInt(1000000); // Generates a random integer up to 1000000
CustomerSession.setCustomerId(customerId);

            // Check if the username already exists
            selectStatement.setString(1, username);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("username already exist");
                    return false;
                }
            }

            // Username doesn't exist, so insert the new user
            // Hash the password
            String hashedPassword = hashPassword(password);

            insertStatement.setString(1, username);
            insertStatement.setString(2, hashedPassword);
            insertStatement.setInt(3, customerId);
            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed due to exception
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static float getDrinkCost(String selectedDrink) throws SQLException {
        float cost = 0.0f;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(); // Assuming you have a method to get a database connection
            String query = "SELECT cost1 FROM beverages WHERE drink_name = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, selectedDrink);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cost = rs.getFloat("cost1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return cost;
    }

    public static float getMealCost(String selectedMeal) throws SQLException {
        float cost = 0.0f;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection(); // Assuming you have a method to get a database connection
            String query = "SELECT cost FROM food WHERE item = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, selectedMeal);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cost = rs.getFloat("cost");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return cost;
    }
}
