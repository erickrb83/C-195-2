package Controller;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 *
 * Stage loaders in one place
 */
public class FXMLLoaders {

    static ResourceBundle bundle = Main.getBundle();

    public void loginStage(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void customerStage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginController.class.getResource("../View/CustomerView.fxml"), bundle);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Customer Table");
        stage.setScene(scene);
        stage.show();
    }

    public static void appointmentScheduleStage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginController.class.getResource("../View/AppointmentView.fxml"), bundle);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Appointment Calendar");
        stage.setScene(scene);
        stage.show();
    }

    public static void reportsStage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginController.class.getResource("../View/ReportsView.fxml"), bundle);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Reports Table");
        stage.setScene(scene);
        stage.show();
    }

    public static void mainMenuStage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginController.class.getResource("../View/MainMenuView.fxml"), bundle);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Reports Table");
        stage.setScene(scene);
        stage.show();
    }

}
