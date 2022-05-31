package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 *
 * The menu after login
 */
public class MainMenuController implements Initializable {

    @FXML
    Button customersButton, appointmentsButton, reportsButton, logoutButton;

    @FXML
    public void customersButtonAction(ActionEvent event) throws IOException {
        FXMLLoaders.customerStage(event);
    }

    @FXML
    public void appointmentsButtonAction(ActionEvent event) throws IOException {
        FXMLLoaders.appointmentScheduleStage(event);
    }

    @FXML
    public void reportsButtonAction(ActionEvent event) throws IOException {
        FXMLLoaders.reportsStage(event);
    }

    @FXML
    public void logoutButtonAction(ActionEvent event) {
        if (Alerts.alertConfirmation(1)){
            System.exit(0);
        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle){

    }
}
