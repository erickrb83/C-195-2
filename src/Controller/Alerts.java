package Controller;

import Main.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 *
 * Streamlined Alerts to one file for ease of changing
 */

public class Alerts {

    static ResourceBundle bundle = Main.getBundle();
    static ResourceBundle rb = bundle;

    /**
     * Overloaded alert to take ID and Type parameter
     * @param alertType
     * @param appointmentID
     * @param appointmentType
     * @return
     * @throws IOException
     */

    protected static boolean alertConfirmation(int alertType, int appointmentID, String appointmentType) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("Alert"));
        switch (alertType) {
            case 5:
                alert.setContentText("Are you sure you want to Delete Appointment #" + appointmentID + ", " + appointmentType);
                Optional<ButtonType> result = alert.showAndWait();
                //if a button is pressed AND it's the OK button, the program exits.
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return true;
                }
                break;
        }
        return false;
    }

    protected static boolean alertConfirmation(int alertType){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("Alert"));
        switch (alertType) {
            case 1:
                alert.setContentText((rb.getString("Logout_Confirm")));
                Optional<ButtonType> result = alert.showAndWait();
                //if a button is pressed AND it's the OK button, the program exits.
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return true;
                }
                break;
            case 2:
                alert.setContentText((rb.getString("Delete_Confirm")));
                result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return true;
                }
                break;
            case 3:
                alert.setContentText((rb.getString("Save_Confirm")));
                result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return true;
                }
                break;
            case 4:
                alert.setContentText((rb.getString("Back_Confirm")));
                result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Overloaded Alter to take appt ID, appt Date, and Time
     * @param alertType
     * @param apptID
     * @param apptDate
     * @param apptTime
     */

    static void displayAlert(int alertType, int apptID, String apptDate, String apptTime){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle((rb.getString("Error")));
        switch (alertType){
            case 4:
                alertError.setHeaderText((rb.getString("Appointment_Reminder")) + "Appointment #:" + apptID + "at: " + apptDate + apptTime);
                alertError.showAndWait();
                break;
        }

        }

    static void displayAlert(int alertType){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle((rb.getString("Error")));
        switch (alertType) {
            case 1:
                alertError.setHeaderText((rb.getString("Select_Error")));
                alertError.showAndWait();
                break;
            case 2:
                alertError.setHeaderText((rb.getString("Missing_Fields")));
                alertError.showAndWait();
                break;
            case 3:
                alertError.setHeaderText((rb.getString("Unable_Delete")));
                alertError.showAndWait();
                break;
            case 5:
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid User ID and/or Password");
                errorAlert.setContentText("Please double check user ID and password");
                errorAlert.showAndWait();
                break;
            case 6:
                alertError.setHeaderText((rb.getString("No_Appointments")));
                alertError.showAndWait();
                break;
            case 7:
                alertError.setHeaderText((rb.getString("Not_During_Business")));
                alertError.showAndWait();
                break;
        }
    }
}

