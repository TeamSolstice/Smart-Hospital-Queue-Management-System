package User;

import javafx.stage.Stage;
import controller.FrontDeskController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class FrontDesk extends Staff {

    public FrontDesk(String staffID, String name, String password) {
        super(staffID, name, password);
    }

    @Override
    public void viewDashboard(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontDeskDashboard.fxml"));
            Parent root = loader.load();
            FrontDeskController fc = loader.getController();
            fc.setFrontDesk(this);
            stage.setScene(new javafx.scene.Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}