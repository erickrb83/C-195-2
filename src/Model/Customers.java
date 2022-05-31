package Model;

import Util.Database;

import java.sql.*;
import java.text.DateFormat;
import java.time.DateTimeException;

public class Customers {

    private int customerID;
    private int divisionID;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private static int nextCustomerID;

    /**
     * Customers Constructor
     *
     * @param customerID
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     * @param divisionID
     */
    public Customers(int customerID, String name, String address, String postalCode, String phone, Timestamp createDate, String createdBy, Timestamp lastUpdate,
                     String lastUpdateBy, int divisionID) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy =createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.divisionID = divisionID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone(){
        return phone;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreateDate(Timestamp createDate){
        this.createDate = createDate;
    }

    public void setCreatedBy (String createdBy){
        this.createdBy =createdBy;
    }

    public void setLastUpdate (Timestamp lastUpdate){
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdateBy (String lastUpdateBy){
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * Creates new customer ID one more than the Max
     * @return
     * @throws SQLException
     */
    public static int newCustomerID() throws Exception {
        //Find last customerID
        Connection conn = Database.openConnection();
        String sql = "SELECT MAX(Customer_ID) from Customers";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        if (rs.next()){
            nextCustomerID = rs.getInt(1);
        }
        //return newCustomerID with + 1
        return ++nextCustomerID;
    }
}
