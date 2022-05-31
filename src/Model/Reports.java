package Model;

import java.time.LocalDateTime;

/**
 * @author Eric Bostard
 */
public class Reports {
    private String appointmentType;
    private String month;
    private int appointmentTotal;

    private String lastUpdatedBy;
    private int updatedID;
    private LocalDateTime lastUpdated;

    /**
     * Reports constructor for appointment type
     * @param appointmentType
     * @param appointmentTotal
     */
    public Reports(String appointmentType, int appointmentTotal){
        this.appointmentType = appointmentType;
        this.appointmentTotal = appointmentTotal;
    }

    /**
     * Overloaded constructor for appointments by Month
     * @param appointmentTotal
     * @param month
     */
    public Reports(int appointmentTotal, String month){
        this.month = month;
        this.appointmentTotal = appointmentTotal;

    }

    /**
     * Overloaded Reports for tables updated by last Update
     * @param lastUpdatedBy
     * @param updatedID
     * @param lastUpdated
     */
    public Reports(String lastUpdatedBy, int updatedID, LocalDateTime lastUpdated){

        this.lastUpdatedBy = lastUpdatedBy;
        this.updatedID = updatedID;
        this.lastUpdated = lastUpdated;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getAppointmentTotal() {
        return appointmentTotal;
    }

    public void setAppointmentTotal(int appointmentTotal) {
        this.appointmentTotal = appointmentTotal;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getUpdatedID() {
        return updatedID;
    }

    public void setUpdatedID(int updatedID) {
        this.updatedID = updatedID;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
