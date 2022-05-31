package Model;

import Util.Database;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * @author Eric Bostard
 */
public class Appointments {
    int appointmentID, customerID, userID, contactID;
    String description, title, location, type, createdBy, lastUpdatedBy;
    LocalDateTime start;
    LocalDateTime end;
    LocalDateTime lastUpdate, createdDate;

    /**
     * Appointment Cunstructor
     *
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createdDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param customerID
     * @param userID
     * @param contactID
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                        LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID
                         ) {
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.description = description;
        this.title = title;
        this.location = location;
        this.type = type;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.start = start;
        this.end = end;
        this.lastUpdate = lastUpdate;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdatedBy(String lastUpdateBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Increments next appointment ID from query of Max ID
     * @return
     * @throws SQLException
     */
    public static int newAppointmentID() throws SQLException {
        int nextAppointmentID= 0;
        //Find last customerID
        Connection conn = Database.openConnection();
        String SQLStatement = "SELECT MAX(Appointment_ID) from Appointments";
        PreparedStatement ps = conn.prepareStatement(SQLStatement);
        ResultSet rs = ps.executeQuery(SQLStatement);
        if (rs.next()){
            nextAppointmentID = rs.getInt(1);
        }
        //return newCustomerID with + 1
        return ++nextAppointmentID;
    }

}
