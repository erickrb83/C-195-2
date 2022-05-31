package Controller;

import DAO.ReportsDAO;
import Model.Appointments;
import Model.Reports;
import Util.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 * Two reports plus custom report
 */

public class ReportsController implements Initializable {

    @FXML
    TableView<Reports> appointmentTypeTable, monthAppointmentTable;
    @FXML
    TableColumn appointmentTypeColumn, appointmentTypeTotalColumn, appointmentMonthColumn,appointmentMonthTotalColumn;
    @FXML
    TableView contactScheduleTable;

    @FXML
    TableColumn contactAppointmentIDColumn, contactTitleColumn, contactTypeColumn, contactDescriptionColumn, contactStartColumn, contactEndColumn, contactCustomerIDColumn;
    @FXML
    ComboBox contactNameComboBox, tableNameCombo;

    @FXML
    TableView recentUpdateTable;
    @FXML
    TableColumn userNameColumn, lastUpdatedColumn, updatedIDColumn;
    @FXML
    RadioButton allRadio, monthRadio;

    @FXML
    Button back;

    /**
     * Initializes and opens Report window
     * Appointments by Type and by Month
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection conn = null;
        try {
            conn = Database.openConnection();
            String typeSQL = ReportsDAO.getTotalAppointmentsByType(conn);
            ObservableList<Reports> appointmentList = ReportsDAO.appointmentsByTypeList(typeSQL, conn);
            appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            appointmentTypeTotalColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
            appointmentTypeTable.setItems(appointmentList);

            appointmentMonthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
            appointmentMonthTotalColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
            String monthSql = ReportsDAO.getTotalAppointmentsByMonth(conn);
            ObservableList<Reports> appointmentMonthList = ReportsDAO.appointmentsByMonthList(monthSql, conn);
            monthAppointmentTable.setItems((appointmentMonthList));
            setContactIDCombo();
            /**
             * Lambda
             * anonymousFunction(Action event){try{lastUpdateReport();
             * }catch(SQLException e)}
             * e.printStackTrace();}
             */
            contactNameComboBox.setOnAction(event -> {
                try {
                    contactScheduleReport();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            setTableNameCombo();
            /**
             * Lambda
             * anonymousFunction(Action event){try{lastUpdateReport();}catch(SQLException e)}e.printStackTrace();}
             */
            tableNameCombo.setOnAction(event -> {
                try {
                    lastUpdateReport();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Custom report by table then last update.
     * @throws SQLException
     */
    public void lastUpdateReport() throws SQLException{
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        updatedIDColumn.setCellValueFactory(new PropertyValueFactory<>("updatedID"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        Connection conn = Database.openConnection();
        ObservableList<Reports> reportList = FXCollections.observableArrayList();
        String selectedTable = tableNameCombo.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectedTable);
        String sql = ReportsDAO.getTable(selectedTable);
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.println(ps);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String userName = rs.getString("Last_Updated_By");
            int tableID = rs.getInt(1);
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

            Reports reportRow = new Reports(userName, tableID, lastUpdate);
            reportList.add(reportRow);
        }
        recentUpdateTable.setItems(reportList);
    }

    /**
     * Contact Schedule by name
     *
     * @throws SQLException
     */
    public void contactScheduleReport() throws SQLException {
        contactAppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        contactTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        contactEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        Connection conn = Database.openConnection();
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        String selectedContact = contactNameComboBox.getSelectionModel().getSelectedItem().toString();
        String sql = ReportsDAO.contactScheduleByName();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, selectedContact);
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
        contactScheduleTable.setItems(appointmentList);
    }

    public void contactNameChange() throws SQLException {
        contactScheduleReport();
    }

    public void setContactIDCombo() throws SQLException {
        ObservableList<String> customerNameList = FXCollections.observableArrayList();
        contactNameComboBox.setItems(null);
        String sql = ReportsDAO.fillCustomerIDCombo();
        Connection conn = Database.openConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customerNameList.add(rs.getString(1));
        }
        contactNameComboBox.setItems(customerNameList);
    }

    public void setTableNameCombo() throws SQLException {
        ObservableList<String> tablesWithUpdates = FXCollections.observableArrayList();
        tableNameCombo.setItems(null);
        String sql = ReportsDAO.tablesWithUpdates();
        Connection conn = Database.openConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            tablesWithUpdates.add(rs.getString("TABLE_NAME"));
        }
        tableNameCombo.setItems(tablesWithUpdates);
    }

    @FXML
    void updateRadioClick(ActionEvent event) throws SQLException {
        if (allRadio.isSelected()) {
            lastUpdateReport();
        } else {
            monthRadioAction();
        }
    }

    public void monthRadioAction() throws SQLException {
        ObservableList<Reports> reportList = FXCollections.observableArrayList();
        String selectedContact = tableNameCombo.getSelectionModel().getSelectedItem().toString();
        int calendar = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Connection conn = Database.openConnection();

        String sql = ReportsDAO.getMonthUpdates(selectedContact);
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, calendar);
        ps.setInt(2, year);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String userName = rs.getString("Last_Updated_By");
            int tableID = rs.getInt(1);
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();

            Reports reportRow = new Reports(userName, tableID, lastUpdate);
            reportList.add(reportRow);
        }
        recentUpdateTable.setItems(reportList);
    }

    @FXML
    public void backButtonAction(ActionEvent event) throws IOException {
        FXMLLoaders.mainMenuStage(event);
    }
}
