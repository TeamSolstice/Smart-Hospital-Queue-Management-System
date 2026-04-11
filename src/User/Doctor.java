package User;

import controller.DoctorController;
import function.SubDepartment;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Doctor extends Staff {
	
	private final SubDepartment subDepartment;
	
	public Doctor(
			String id, 
			String name, 
			String password, 
			SubDepartment subDepartment) {
		super(id, name, password);
		this.subDepartment = subDepartment;
	}
	
	@Override
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
	
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}
}
