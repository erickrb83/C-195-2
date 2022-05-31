package Controller;

import DAO.AppointmentsDAO;
import Model.Appointments;
import Util.DateTime;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import Util.Database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * @author Eric Bostard
 */

public class LoginController implements Initializable {

    public static String userName;

    private ZoneId localZoneId = ZoneId.systemDefault();

    @FXML
    public TextField userID, password, locationText, languageText;

    @FXML
    public Label usernameLabel, passwordLabel, locationLabel, languageLabel;

    void setUserName(String userName){
        this.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }

    String lang = "English";

    @FXML
    public void loginButtonClicked(ActionEvent event) throws IOException, SQLException {
        boolean logFlag;
        userName = userID.getText();
        String userPassword = password.getText();
        if(checkUserPW(event, userName, userPassword)){
            logFlag = false;
            //compare login time and userID to Appointments and their time within 15 minutes of Login Time
            appointmentChecker();
        } else {
            logFlag = true;
        }
        setUserName(userName);
        //Get login time
        LocalDateTime loginTime = LocalDateTime.now();
        DateTime.convertToEST(loginTime);
        logActivity(logFlag ,userName);
    }

    public boolean checkUserPW (ActionEvent event, String userName, String password) throws IOException, SQLException {
        //create statement object
        Connection conn = Database.openConnection();
        Statement statement = conn.createStatement();
        //write SQL statement
        String sqlStatement = "SELECT * FROM users WHERE User_Name ='" +userName+ "' and Password = '"+password+"' ";
        //create result object
        ResultSet rs = statement.executeQuery(sqlStatement);
        if (rs.next()){
            customerController(event);
            Alerts.displayAlert(4);
            return true;
        } else {
            Alerts.displayAlert(5);
            return false;
        }
    }

    /**
     * Fills out login_activity about all login attempts, successful, or unsuccessful
     *
     * @param logFlag
     * @param user
     * @throws IOException
     */
    private void logActivity(boolean logFlag, String user){
        try {
            FileWriter writer = new FileWriter("login_activity.txt", true);
            BufferedWriter bWriter = new BufferedWriter(writer);
            ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
            String successfulLogin = "User '" + user + "' successfully logged in on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + localZoneID + " time\n";
            String unknownUserLogin = "Unknown User " + user + " failed to log in on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + localZoneID + " time\n";
            String failedLogin = "User '" + user + "' failed to log in on " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + localZoneID + " time\n";
            // if login is successful add it to login_activity.txt file
            if (!logFlag) {
                bWriter.write(successfulLogin);

            } else if (user.isBlank()) {
                // if the user is blank add unknown user login_activity.txt
                bWriter.write(unknownUserLogin);

            } else {
                // if the login failed add failed user to login_activity.txt
                bWriter.write(failedLogin);
            }
            bWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Checks for any appointments that are within 15 minutes of login
     * @throws SQLException
     */
    private void appointmentChecker () throws SQLException {
        Connection conn = Database.openConnection();
        ObservableList<Appointments> appointmentList = AppointmentsDAO.getAllAppointments(conn);
        boolean appointmentFlag  = false;
        int apptID = -1;
        String apptDate ="";
        String apptTime = "";

        // check to see if an appointment occurs within 15 minutes of current time
        LocalTime currentTime = LocalTime.now();
        // search through all appointments in the database to see if an appointment is within 15 minutes
        for(Appointments appointment : appointmentList) {
            LocalTime appointmentStartTime = appointment.getStart().toLocalTime();
            Long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointmentStartTime);
            //if the current day matches today and the time is within 15 minutes
            if (appointment.getStart().toLocalDate().equals(LocalDate.now())) {
                if(timeDifference > -1 && timeDifference <= 15) {
                    appointmentFlag = true;
                    apptID = appointment.getAppointmentID();
                    apptDate = DateTime.formatDate(appointment.getStart());
                    apptTime = DateTime.formatToAMPM(appointment.getStart());
                    break;
                }
            }
        }
        // If there is an appointment set an alert with the appointment information, otherwise alert says no appointments
        if(appointmentFlag) {
            Alerts.displayAlert(4, apptID, apptDate, apptTime);
        } else {
            Alerts.displayAlert(6);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        setLanguageText();
        setMyLocale();
    }

    public static void customerController(ActionEvent event) throws IOException {
        FXMLLoaders.mainMenuStage(event);
    }

    /**
     * Sets location
     */
    public void setMyLocale(){
        locationText.setText(localZoneId.toString());
    }

    /**
     * Sets Language based on location
     */
    public void setLanguageText (){
        Locale myLocale = Locale.getDefault();
        lang = myLocale.getDisplayLanguage();
        languageText.setText(lang);
    }
}
