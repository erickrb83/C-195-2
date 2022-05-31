package Model;

import java.sql.*;

/**
 * @author Eric Bostard
 */

public class FirstLevelDivisions {
    int divisionID, countryID;

    String division, createdBy, lastUpdateBy;

    Timestamp createDate, lastUpdate;

    /**
     * First Level Constructor
     * @param divisionID
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     * @param countryID
     */
    public FirstLevelDivisions(int divisionID, String division, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy, int countryID) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.division = division;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
