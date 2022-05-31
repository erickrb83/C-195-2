package Controller;

import DAO.CountryDAO;
import DAO.CustomersDAO;
import DAO.FirstLevelDivisionsDAO;
import Model.Customers;
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
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 */

public class CustomerController implements Initializable {
    @FXML
    public TableView<Customers> customerTableview;
    @FXML
    public TableColumn<Customers, Integer> customerIDColumn;
    @FXML
    public TableColumn <Customers, String> nameColumn;
    @FXML
    public TableColumn <Customers, String> addressColumn;
    @FXML
    public TableColumn <Customers, String> postalColumn;
    @FXML
    public TableColumn <Customers, String> phoneColumn;
    @FXML
    public TableColumn <Customers, LocalDateTime> createdDateColumn;
    @FXML
    public TableColumn <Customers, String> createdByColumn;
    @FXML
    public TableColumn <Customers, LocalDateTime> lastUpdateColumn;
    @FXML
    public TableColumn <Customers, String> lastUpdateByColumn;
    @FXML
    public TableColumn <Customers, Integer> divisionIDColumn;

    @FXML
    public TextField nameText, addressText, postalCodeText, phoneText;

    @FXML
    public Button addCustomer, updateCustomer, deleteCustomer, appointmentSchedule, logout;

    @FXML
    public ComboBox<String> countryCombo, stateCombo;

    int divisionID;

    @FXML
    void appointmentScheduleAction(ActionEvent event) throws IOException {
        FXMLLoaders.appointmentScheduleStage(event);
    }

    /**
     * Adds customers with logic checks
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void addCustomerAction(ActionEvent event) throws Exception, IOException {
        if (nameText.getText().isEmpty() || addressText.getText().isEmpty() || postalCodeText.getText().isEmpty() || phoneText.getText().isEmpty() ||
                countryCombo.getPromptText() == "Country" || stateCombo.getPromptText() == "State"){
            Alerts.displayAlert(2);
            return;
        }
        if(!Alerts.alertConfirmation(3)){
            return;
        }
        Connection conn = Database.openConnection();
        String sql = CustomersDAO.insertCustomer(conn);

        customerInputs(sql, conn);

        ObservableList<Customers> refreshCustomersList = CustomersDAO.getAllCustomers(conn);
        customerTableview.setItems(refreshCustomersList);
        clearTextFields();
    }

    /**
     * Updates customers with logic checks
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void updateCustomerAction(ActionEvent event) throws SQLException, IOException {
        if(customerTableview.getSelectionModel().getSelectedItem() == null){
            Alerts.displayAlert(1);
            return;
        }
        if (nameText.getText().isEmpty() || addressText.getText().isEmpty() || postalCodeText.getText().isEmpty() || phoneText.getText().isEmpty() ||
                countryCombo.getPromptText() == "Country" || stateCombo.getPromptText() == "State"){
            Alerts.displayAlert(2);
            return;
        }
        if(!Alerts.alertConfirmation(3)){
            return;
        }

        int customerID = customerTableview.getSelectionModel().getSelectedItem().getCustomerID();

        Connection conn = Database.openConnection();
        String sql = CustomersDAO.updateCustomer(conn);
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, nameText.getText());
        ps.setString(2, addressText.getText());
        ps.setString(3, postalCodeText.getText());
        ps.setString(4, phoneText.getText());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, LoginController.getUserName());
        ps.setInt(7, customerID);

        ps.execute();

        ObservableList<Customers> refreshCustomersList = CustomersDAO.getAllCustomers(conn);
        customerTableview.setItems(refreshCustomersList);

        clearTextFields();
    }

    /**
     * Deletes customers with confirmation checks
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void deleteCustomerAction (ActionEvent event) throws SQLException, IOException {
        if(customerTableview.getSelectionModel().getSelectedItem() == null){
            Alerts.displayAlert(1);
            return;
        }
        if(!Alerts.alertConfirmation(2)){
            return;
        }
        int customerID = customerTableview.getSelectionModel().getSelectedItem().getCustomerID();

        if (checkForAppointments()){
            Alerts.displayAlert(3);
            return;
        }

        Connection conn = Database.openConnection();
        String sql = CustomersDAO.deleteCustomer(conn);

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, customerID);

        ps.executeUpdate();

        ObservableList<Customers> refreshCustomersList = CustomersDAO.getAllCustomers(conn);
        customerTableview.setItems(refreshCustomersList);
        clearTextFields();
    }

    /**
     * Checks to see if appointments are associated with customers
     * @return
     * @throws SQLException
     */
    public boolean checkForAppointments() throws SQLException {
        Connection conn = Database.openConnection();
        String sql = CustomersDAO.checkCustomerAppointments(conn);
        PreparedStatement ps = conn.prepareStatement(sql);
        int customerID = customerTableview.getSelectionModel().getSelectedItem().getCustomerID();
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        int customerAppointment = 0;
        while (rs.next()) {
            customerAppointment = rs.getInt("Customer_ID");
        }
        if (customerID == customerAppointment) {
            return true;
        }
        return false;
    }

    @FXML
    void logoutAction (ActionEvent event){
        if (Alerts.alertConfirmation(1)){
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection conn = Database.openConnection();
            ObservableList<Customers> customerList = CustomersDAO.getAllCustomers(conn);

            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
            createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
            lastUpdateByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
            divisionIDColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

            customerTableview.setItems(customerList);

            setCountryCombo();
            /**
             * Below is a LAMBDA expression as a method reference
             */
            countryCombo.setOnAction(this::getCountryName);
            stateCombo.setOnAction(this::getDivisionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCountryName(ActionEvent event) {
        String country = countryCombo.getValue();
        try{
            setStateCombo(country);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCountryCombo() throws SQLException {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        countryCombo.setItems(null);

        String sql = CountryDAO.fillCountryCombo();
        Connection conn = Database.openConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            countryList.add(rs.getString(1));
        }
        countryCombo.setItems(countryList);
    }

    void setStateCombo(String country) throws Exception{
        ObservableList<String> stateList;
        stateCombo.setItems(null);
        Connection conn = Database.openConnection();

        int countryID = CountryDAO.findCountryID(conn, country);
        stateList = FirstLevelDivisionsDAO.fillStateCombo(countryID);

        stateCombo.setItems(stateList);
    }

    public int getDivisionID(ActionEvent event) {
        String state = stateCombo.getValue();
        try {
            divisionID = FirstLevelDivisionsDAO.setDivisionID(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDivisionID(divisionID);
        return divisionID;
    }

    public void setDivisionID(int divisionID){
        this.divisionID = divisionID;
    }

    public void customerInputs(String sql, Connection conn) throws Exception{
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, Customers.newCustomerID());
        ps.setString(2, nameText.getText());
        ps.setString(3, addressText.getText());
        ps.setString(4, postalCodeText.getText());
        ps.setString(5, phoneText.getText());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, LoginController.getUserName());
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, LoginController.getUserName());
        ps.setInt(10, divisionID);

        ps.execute();
    }

    @FXML
    public void selectCustomerClick(javafx.scene.input.MouseEvent mouseEvent) throws Exception {
        Customers selectedCustomer = customerTableview.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null){
            return;
        }

        divisionID = selectedCustomer.getDivisionID();
        String divisionName = FirstLevelDivisionsDAO.getDivisionName(divisionID);
        String countryName = FirstLevelDivisionsDAO.getCountryName(divisionID);

        nameText.setText(selectedCustomer.getName());
        addressText.setText(selectedCustomer.getAddress());
        postalCodeText.setText(selectedCustomer.getPostalCode());
        phoneText.setText(selectedCustomer.getPhone());
        countryCombo.setPromptText(divisionName);
        stateCombo.setPromptText(countryName);
    }

    @FXML
    void mainMenuAction(ActionEvent event) throws IOException {
        FXMLLoaders.mainMenuStage(event);
    }

    void clearTextFields() throws SQLException {
        nameText.clear();
        addressText.clear();
        postalCodeText.clear();
        phoneText.clear();
        stateCombo.setPromptText("State");
        countryCombo.setPromptText("Country");
    }
}