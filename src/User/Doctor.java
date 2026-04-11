package User;

import javafx.stage.Stage;
import controller.DoctorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Doctor extends Staff {

    private String specialization;

    public Doctor(String staffID, String name, String password, String specialization) {
        super(staffID, name, password);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void viewDashboard(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DoctorDashboard.fxml"));
            Parent root = loader.load();
            DoctorController dc = loader.getController();
            dc.setDoctor(this);
            stage.setScene(new javafx.scene.Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
