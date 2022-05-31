package DAO;

import Model.Countries;
import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Country Data Access Object with SQL queries
 */
public class CountryDAO {
    public static String fillCountryCombo(){
        String sql = "SELECT `Country` FROM `countries`";
        return sql;
    }

    public static int findCountryID(Connection conn, String country) throws SQLException {
        int countryID = 0;
        String sql = "SELECT Country_ID FROM countries WHERE Country = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            countryID = rs.getInt(1);
        }
        return countryID;
    }

    public static ObservableList<Countries> countryList(String sql, Connection conn) throws SQLException {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String Country = rs.getString("Country");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdateBy = rs.getString("Last_Updated_By");

            Countries countryRow = new Countries(countryID, Country, createDate, createdBy, lastUpdate, lastUpdateBy);
            countryList.add(countryRow);
        }
        return countryList;
    }
}
