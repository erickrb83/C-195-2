package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Customers;

import java.sql.*;

/**
 * Customer Data Access with SQL queries
 *
 */

public class CustomersDAO {

    public Customers getCustomer(Connection conn, int custID) throws SQLException {
        String sql = "SELECT * FROM Customers WHERE Customer_ID = ?";
        Customers customer = (Customers) customerList(sql, conn);

        return customer;
    }

    public static ObservableList<Customers> getAllCustomers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Customers";

        ObservableList<Customers> customerList = customerList(sql, conn);

        return customerList;
    }

    public static String insertCustomer(Connection conn) throws SQLException {
        String sql = "INSERT INTO Customers (customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return sql;
    }

    public static String updateCustomer(Connection conn) throws SQLException {
        String sql = "UPDATE Customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ? " +
                "WHERE Customer_ID = ?";
        return sql;
    }

    public static String deleteCustomer(Connection conn) throws SQLException{
        String sql = "DELETE FROM Customers WHERE Customer_ID = ?";
        return sql;
    }

    public static String checkCustomerAppointments(Connection conn) throws SQLException{
        String sql = "SELECT Customer_ID FROM Appointments WHERE Customer_ID = ?";
        return sql;
    }

    public static ObservableList<Customers> customerList(String sql, Connection conn) throws SQLException {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdateBy = rs.getString("Last_Updated_By");
            int divisionID = rs.getInt("Division_ID");

            Customers customerRow = new Customers(customerID, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy,
                    divisionID);
            customerList.add(customerRow);
        }
        return customerList;
    }

    /** Has to be static to be here, put into Customer Controller.
     public void customerInputs(String sql, Connection conn) throws SQLException{
     PreparedStatement ps = conn.prepareStatement(sql);
     ps.setInt(1, Integer.parseInt(CustomerController.customerIDText.getText()));
     ps.setString(2, CustomerController.nameText.getText());
     ps.setString(3, CustomerController.addressText.getText());
     ps.setString(4, CustomerController.postalCodeText.getText());
     ps.setString(5, CustomerController.phoneText.getText());
     ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
     ps.setString(7, CustomerController.lastUpdatedByText.getText());
     ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
     ps.setString(9, CustomerController.lastUpdatedByText.getText());

     ps.execute();
     }
     */
}
