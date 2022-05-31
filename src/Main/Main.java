package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Util.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Eric Bostard
 */

public class Main extends Application {

    String lang = "English";
    static ResourceBundle bundle;

    /**
     * Switches between English and French for alerts, and for buttons, columns, etc.
     */
    public void setBundle() {
        Locale myLocale = Locale.getDefault();
        lang = myLocale.getDisplayLanguage();
        bundle = ResourceBundle.getBundle("Util/" + lang);
    }

    public static ResourceBundle getBundle(){
        return bundle;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setBundle();
        bundle = getBundle();
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"), bundle);
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        Connection conn = Database.openConnection();
        launch(args); //launch is inherited from Application
        Database.closeConnection(conn);
    }
}
