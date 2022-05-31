package DAO;

import Model.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reports Data Access with SQL queries
 */
public class ReportsDAO {

    public static String getTotalAppointmentsByType(Connection conn){
        String sql = "SELECT type, count(*) AS total FROM appointments GROUP by type ORDER BY type";
        return sql;
    }

    public static String getTotalAppointmentsByMonth(Connection conn){
        String sql = "SELECT CONCAT(MONTHNAME(`Start`),' ', YEAR(`Start`)) AS Month ,COUNT(*) as Total FROM appointments GROUP BY MONTH";
        return sql;
    }

    public static ObservableList<Reports> appointmentsByTypeList(String sql, Connection conn) throws SQLException{
        ObservableList<Reports> typeList = FXCollections.observableArrayList();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String type = rs.getString("type");
            int total = rs.getInt("total");

            Reports byTypeRow = new Reports(type, total);
            typeList.add(byTypeRow);
        }
        return typeList;
    }

    public static ObservableList<Reports> appointmentsByMonthList(String sql, Connection conn) throws SQLException{
        ObservableList<Reports> monthList = FXCollections.observableArrayList();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String start = rs.getString("Month");
            int total = rs.getInt("Total");

            Reports byMonthRow = new Reports(total, start);
            monthList.add(byMonthRow);
        }
        return monthList;
    }

    public static String fillCustomerIDCombo(){
        String sql = "SELECT Contact_Name FROM contacts";
        return sql;
    }

    public static String contactScheduleByName(){
        String sql = "SELECT * FROM appointments INNER JOIN contacts on appointments.Contact_ID = contacts.Contact_ID WHERE contacts.Contact_Name = ?";
        return sql;
    }

    public static String tablesWithUpdates(){
        String sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME LIKE '%Last_Updated_By%' ORDER BY TABLE_NAME";
        return sql;
    }

    //Tostring is making it not work.
    public static String getTable(String tableName){
        String sql = "SELECT * FROM " + tableName;
        return sql;
    }

    //Same here
    public static String getMonthUpdates(String tableName){
        String sql = "SELECT * FROM " +tableName + " WHERE MONTH(`Last_Update`) = ? AND YEAR(`Last_Update`) = ?";
        return sql;
    }
}
