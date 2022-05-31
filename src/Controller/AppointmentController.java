package Controller;

import DAO.AppointmentsDAO;
import Model.Appointments;
import Util.DateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import Util.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 */

public class AppointmentController implements Initializable {
    @FXML
    TableView<Appointments> appointmentTableview;

    @FXML
    TableColumn<?, ?> appointmentIDColumn;
    @FXML
    TableColumn<?, ?> titleColumn;
    @FXML
    TableColumn<?, ?> descColumn;
    @FXML
    TableColumn<?, ?> locColumn;
    @FXML
    TableColumn<?, ?> typeColumn;
    @FXML
    TableColumn<?, ?> startColumn;
    @FXML
    TableColumn<?, ?> endColumn;
    @FXML
    TableColumn<?, ?> createdDateColumn;
    @FXML
    TableColumn<?, ?> createdByColumn;
    @FXML
    TableColumn<?, ?> lastUpdateColumn;
    @FXML
    TableColumn<?, ?> lastUpdatedByColumn;
    @FXML
    TableColumn<?, ?> customerIDColumn;
    @FXML
    TableColumn<?, ?> contactIDColumn;
    @FXML
    TableColumn<?, ?> userIDColumn;

    @FXML
    TextField titleText, descText, locText, typeText;

    @FXML
    RadioButton allApptRadio, monthApptRadio, weekApptRadio;

    @FXML
    DatePicker dateDP;

    @FXML
    ComboBox<Integer> customerIDCombo;
    @FXML
    ComboBox<Integer> userIDCombo;
    @FXML
    ComboBox<String> contactIDCombo;
    @FXML
    ComboBox<String> startTimeCombo, endTimeCombo;

    static ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

    int nextApptNumber = 0;

    /**
     * Adds new appointments
     * Checks if it's during working EST Hours
     *
     * Converts to UTC for saving
     *
     * checks for any empty space
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    void addAppointmentAction (ActionEvent event) throws SQLException {
        if(titleText.getText().isEmpty() || locText.getText().isEmpty() || typeText.getText().isEmpty() || descText.getText().isEmpty() ||
                customerIDCombo.getSelectionModel().isEmpty() || userIDCombo.getSelectionModel().isEmpty() || startTimeCombo.getSelectionModel().isEmpty() ||
                endTimeCombo.getSelectionModel().isEmpty() || contactIDCombo.getSelectionModel().isEmpty()){
            Alerts.displayAlert(2);
            return;
        }
        if(!Alerts.alertConfirmation(3)){
            return;
        }
        LocalDate localDateEnd = dateDP.getValue();
        LocalDate localDateStart = dateDP.getValue();

        DateTimeFormatter minHourFormat = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime localTimeStart = LocalTime.parse(startTimeCombo.getValue(), minHourFormat);
        LocalTime LocalTimeEnd = LocalTime.parse(endTimeCombo.getValue(), minHourFormat);

        LocalDateTime dateTimeStart = LocalDateTime.of(localDateStart, localTimeStart);
        LocalDateTime dateTimeEnd = LocalDateTime.of(localDateEnd, LocalTimeEnd);

        ZonedDateTime zoneDtStart = ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault());
        ZonedDateTime zoneDtEnd = ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault());

        ZonedDateTime convertStartEST = zoneDtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime convertEndEST = zoneDtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

        if(convertStartEST.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue()) || convertStartEST.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ||
                convertEndEST.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SATURDAY.getValue())  || convertEndEST.toLocalDate().getDayOfWeek().getValue() == (DayOfWeek.SUNDAY.getValue()) ){
            Alerts.displayAlert(6);
            return;
        }

        if(convertStartEST.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || convertStartEST.toLocalTime().isAfter(LocalTime.of(22, 0, 0)) ||
                convertEndEST.toLocalTime().isBefore(LocalTime.of(8, 0, 0)) || convertEndEST.toLocalTime().isAfter(LocalTime.of(22, 0, 0))){
            Alerts.displayAlert(6);
            return;
        }

        Connection conn = Database.openConnection();
        String sql = AppointmentsDAO.insertAppointment(conn);

        appointmentInputs(sql, conn);

        ObservableList<Appointments> refreshAppointmentList = AppointmentsDAO.getAllAppointments(conn);
        appointmentTableview.setItems(refreshAppointmentList);

        clearTextFields();
    }

    /**
     * Updates appointments Same checks as Save appointments
     * @param event
     * @throws SQLException
     */
    @FXML
    void updateAppointmentAction(ActionEvent event) throws SQLException{
        if(appointmentTableview.getSelectionModel().getSelectedItem() == null){
            Alerts.displayAlert(1);
            return;
        }
        if (titleText.getText().isEmpty() || locText.getText().isEmpty() || typeText.getText().isEmpty() || descText.getText().isEmpty() ||
                customerIDCombo.getSelectionModel().isEmpty() || userIDCombo.getSelectionModel().isEmpty() || startTimeCombo.getSelectionModel().isEmpty() ||
                endTimeCombo.getSelectionModel().isEmpty() || contactIDCombo.getSelectionModel().isEmpty()){
            Alerts.displayAlert(2);
            return;
        }
        if (!Alerts.alertConfirmation(3)){
            return;
        }

        int appointmentID = appointmentTableview.getSelectionModel().getSelectedItem().getAppointmentID();

        Connection conn = Database.openConnection();
        String sql = AppointmentsDAO.updateAppointment(conn);
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, titleText.getText());
        ps.setString(2, descText.getText());
        ps.setString(3, locText.getText());
        ps.setString(4, typeText.getText());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, LoginController.getUserName());
        ps.setInt(9, Integer.parseInt(customerIDCombo.getPromptText()));
        ps.setInt(10, Integer.parseInt(userIDCombo.getPromptText()));
        ps.setInt(11, Integer.parseInt(contactIDCombo.getPromptText()));
        ps.setInt(12, appointmentID);

        ps.execute();

        ObservableList<Appointments> refreshAppointmentList = AppointmentsDAO.getAllAppointments(conn);
        appointmentTableview.setItems(refreshAppointmentList);

        clearTextFields();
    }

    /**
     * Deletes appointments
     *
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void deleteAppointmentAction(ActionEvent event) throws IOException, SQLException {
        if(appointmentTableview.getSelectionModel().getSelectedItem() == null){
            Alerts.displayAlert(1);
            return;
        }

        int appointmentID = appointmentTableview.getSelectionModel().getSelectedItem().getCustomerID();
        String appointmentType = appointmentTableview.getSelectionModel().getSelectedItem().getType();

        if(!Alerts.alertConfirmation(5, appointmentID, appointmentType)){
            return;
        }

        Connection conn = Database.openConnection();
        String sql = AppointmentsDAO.deleteAppointment(conn);

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, appointmentID);

        ps.executeUpdate();

        ObservableList<Appointments> refreshAppointmentList = AppointmentsDAO.getAllAppointments(conn);
        appointmentTableview.setItems(refreshAppointmentList);
        clearTextFields();
    }

    @FXML
    void apptRadioClick(ActionEvent event) throws SQLException {
        if (allApptRadio.isSelected()) {
            allApptRadioSelected();
        } else if (monthApptRadio.isSelected()) {
            monthApptRadioSelected();
        } else {
            weekApptRadioSelected();
        }
    }

    /**
     * All Appointments selected
     * @throws SQLException
     */
    void allApptRadioSelected() throws SQLException {
        Connection conn = Database.openConnection();
        ObservableList<Appointments> appointmentList = AppointmentsDAO.getAllAppointments(conn);

        appointmentTableview.setItems(appointmentList);
    }

    /**
     * Month selected
     *
     * Lambda to shorten and streamline code.
     *
     * @throws SQLException
     */
    void monthApptRadioSelected(){
        ObservableList<Appointments> allAppointmentsList = appointmentList;
        ObservableList<Appointments> appointmentsMonth = FXCollections.observableArrayList();

        LocalDateTime currentMonthStart = LocalDateTime.now().minusMonths(1);
        LocalDateTime currentMonthEnd = LocalDateTime.now().plusMonths(1);


        if (allAppointmentsList != null) {
            /**
             * Lambda
             */
            allAppointmentsList.forEach(appointment -> {
                if (appointment.getEnd().isAfter((currentMonthStart)) && appointment.getEnd().isBefore(currentMonthEnd)) {
                    appointmentsMonth.add(appointment);
                }
                appointmentTableview.setItems(appointmentsMonth);
            });
        }
    }

    /**
     * week selected
     *
     * Lambda to shorten and streamline code.
     *
     * @throws SQLException
     */
    void weekApptRadioSelected() {
        ObservableList<Appointments> allAppointmentsList = appointmentList;
        ObservableList<Appointments> appointmentsWeek = FXCollections.observableArrayList();

        LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
        LocalDateTime weekEnd = LocalDateTime.now().plusWeeks(1);

        if (allAppointmentsList != null)
        /**
         * Lambda
         */
            allAppointmentsList.forEach(appointment -> {
                if (appointment.getEnd().isAfter(weekStart) && appointment.getEnd().isBefore(weekEnd)) {
                    appointmentsWeek.add(appointment);
                }
                appointmentTableview.setItems(appointmentsWeek);
            });
    }

    /**
     * Gathers information on selected appointment
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void selectAppointmentClick(MouseEvent event) throws SQLException {
        Appointments selectedAppointment = appointmentTableview.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null){
            return;
        }
        titleText.setText(selectedAppointment.getTitle());
        descText.setText(selectedAppointment.getDescription());
        locText.setText(selectedAppointment.getLocation());
        typeText.setText(selectedAppointment.getType());
        dateDP.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeCombo.getSelectionModel().select(String.valueOf(selectedAppointment.getStart().toLocalTime()));
        endTimeCombo.getSelectionModel().select(String.valueOf(selectedAppointment.getEnd().toLocalTime()));
        customerIDCombo.setPromptText(String.valueOf(selectedAppointment.getCustomerID()));
        userIDCombo.setPromptText(String.valueOf(selectedAppointment.getUserID()));
        contactIDCombo.setPromptText(String.valueOf(selectedAppointment.getContactID()));
    }

    public void initialize (URL url, ResourceBundle resourceBundle){
        try {
            Connection conn = Database.openConnection();
            ObservableList<Appointments> appointmentList = AppointmentsDAO.getAllAppointments(conn);

            appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
            createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
            lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
            contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));

            appointmentTableview.setItems(appointmentList);
            nextApptNumber = Appointments.newAppointmentID();

            setCustomerCombo();
            setUserIDCombo();
            setContactIDCombo();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        DateTime.populateTimes(startTimeCombo, start, end);
        DateTime.populateTimes(endTimeCombo, start, end);
    }

    public void setUserIDCombo() throws SQLException {
        ObservableList<Integer> userIDList = FXCollections.observableArrayList();
        userIDCombo.setItems(null);

        String sql = AppointmentsDAO.fillUserIDCombo();
        Connection conn = Database.openConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            userIDList.add(rs.getInt(1));
        }
        userIDCombo.setItems(userIDList);
    }

    public void setContactIDCombo() throws SQLException {
        ObservableList<String> contactIDList = FXCollections.observableArrayList();
        contactIDCombo.setItems(null);

        String sql = AppointmentsDAO.fillContactIDCombo();
        Connection conn = Database.openConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            contactIDList.add(rs.getString(1));
        }
        contactIDCombo.setItems(contactIDList);
    }

    public void setCustomerCombo() throws SQLException {
        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
        customerIDCombo.setItems(null);

        String sql = AppointmentsDAO.fillCustomerIDCombo();
        Connection conn = Database.openConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customerIDList.add(rs.getInt(1));
        }
        customerIDCombo.setItems(customerIDList);
    }

    /**
     * Gathers all inputs for appointments
     *
     * @param sql
     * @param conn
     * @throws SQLException
     */
    public void appointmentInputs(String sql, Connection conn) throws SQLException {
        String startDate = dateDP.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startTime = String.valueOf(startTimeCombo.getValue());

        String endDate = dateDP.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endTime = String.valueOf(endTimeCombo.getValue());

        String startUTC = DateTime.convertToUTC(startDate + " " + startTime + ":00");
        String endUTC = DateTime.convertToUTC(endDate + " " + endTime + ":00");

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, nextApptNumber);
        ps.setString(2, titleText.getText());
        ps.setString(3, descText.getText());
        ps.setString(4, locText.getText());
        ps.setString(5, typeText.getText());
        ps.setTimestamp(6, Timestamp.valueOf(startUTC));
        ps.setTimestamp(7, Timestamp.valueOf(endUTC));
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, LoginController.getUserName());
        ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(11, LoginController.getUserName());
        ps.setInt(12, Integer.parseInt(String.valueOf(customerIDCombo.getValue())));
        ps.setInt(13, Integer.parseInt(String.valueOf(userIDCombo.getValue())));
        ps.setInt(14, Integer.parseInt(String.valueOf(contactIDCombo.getValue())));

        ps.execute();
    }

    @FXML
    void mainMenuAction (ActionEvent event) throws IOException {
        FXMLLoaders.mainMenuStage(event);
    }

    @FXML
    public void reportsButtonAction(ActionEvent event) throws IOException {
        FXMLLoaders.reportsStage(event);
    }

    @FXML
    void backAction(ActionEvent event) throws IOException {
        if (!Alerts.alertConfirmation(4)){
            return;
        }
        FXMLLoaders.customerStage(event);
    }

    /**
     *  Clear all inputs as a function to avoid repetition.
     */
    void clearTextFields(){
        titleText.clear();
        locText.clear();
        typeText.clear();
        descText.clear();
        dateDP.getPromptText();
        startTimeCombo.setPromptText("Start Time");
        endTimeCombo.setPromptText("End Time");
        userIDCombo.setPromptText("User ID");
        customerIDCombo.setPromptText("Customer ID");
        contactIDCombo.setPromptText("Contact ID");
    }
}
