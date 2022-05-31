package DAO;

import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access for Appointments with SQL queries
 */
public class AppointmentsDAO {

    public Appointments getAppointment(Connection conn, int apptID) throws SQLException{
        String sql = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        Appointments appointment = (Appointments) appointmentList(sql, conn) ;

        return appointment;
    }

    public static ObservableList<Appointments> getAllAppointments(Connection conn) throws SQLException {
        String sql = "SELECT * FROM appointments";

        ObservableList<Appointments> appointmentList = appointmentList(sql, conn);

        return appointmentList;
    }


    public static String insertAppointment(Connection conn) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End," +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ? , ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?)";
        return sql;
    }

    public static String updateAppointment(Connection conn) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                     "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ? , User_ID = ?, Contact_ID = ? " +
                    "WHERE Appointment_ID = ?";
        return sql;
    }

    public static String deleteAppointment(Connection conn) throws SQLException{
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        return sql;
    }

    public static ObservableList<Appointments> appointmentList(String sql, Connection conn) throws SQLException {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");


            Appointments appointmentRow = new Appointments(appointmentID, title, description, location, type, start, end,
                    createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);
            appointmentList.add(appointmentRow);
        }
        return appointmentList;
    }

    public static String fillCustomerIDCombo(){
        String sql = "SELECT Customer_ID FROM Customers";
        return sql;
    }
    public static String fillUserIDCombo(){
        String sql = "SELECT User_ID FROM Users";
        return sql;
    }
    public static String fillContactIDCombo(){
        String sql = "SELECT Contact_ID FROM contacts";
        return sql;
    }
}
