package User;

import function.SubDepartment;
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
	
	public void viewDashboard(Stage stage) {
		// TODO
	}
	
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}
}
