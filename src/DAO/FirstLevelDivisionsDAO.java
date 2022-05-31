package DAO;

import Util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * First Level Division Data Access with SQL queries
 */

public class FirstLevelDivisionsDAO {

    public static ObservableList<String> fillStateCombo(int countryID) throws Exception {
        ObservableList<String> stateList = FXCollections.observableArrayList();
        Connection conn = Database.openConnection();
        String sql = "SELECT Division FROM first_level_Divisions WHERE Country_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            stateList.add(rs.getString(1));
        }
        return stateList;
    }

    public static int setDivisionID(String state) throws Exception {
        Connection conn = Database.openConnection();
        String sql = "SELECT Division_ID FROM first_level_Divisions WHERE Division = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, state);
        ResultSet rs = ps.executeQuery();
        int divisionID = 0;
        while(rs.next()) {
            divisionID = (rs.getInt(1));
        }
        return divisionID;
    }

    public static String getDivisionName(int divisionID) throws Exception {
        String divisionName = null;
        Connection conn = Database.openConnection();
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            divisionName = rs.getString(1);
        }
        return divisionName;
    }

    public static String getCountryName(int divisionID) throws Exception {
        String countryName = null;
        Connection conn = Database.openConnection();
        String sql = "SELECT Country FROM countries INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.COUNTRY_ID\n" +
                "WHERE first_level_divisions.division_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            countryName = rs.getString(1);
        }
        return countryName;
    }
}
