package application;

import User.Staff;
import User.Patient;
import User.Doctor;
import User.FrontDesk;
import function.PatientQueue;
import function.SubDepartment;
import function.ConsultationRecord;
import function.Department;
import function.TimeSlot;

import java.util.*;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;

public class Main extends Application {

    public static List    <Department>      departments = new ArrayList<>(); //List of Department
    public static HashMap <String, Staff>   authMap     = new HashMap<>();   //Map for Staff Auth
    public static HashMap <String, Patient> patientMap  = new HashMap<>();   //Map for patient login, no auth required 
    public static PatientQueue              scanQueue           = new PatientQueue();
    public static List<ConsultationRecord>  consultationRecords = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception {
        demo();
        String[] titles = {"Login - 1", "Login - 2", "Login - 3"};
        int[] xPos = {0, 660, 1320};
        
        for (int i = 0; i < 3; i++) {
            Stage stage = (i == 0) ? primaryStage : new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            stage.setTitle(titles[i]);
            stage.setScene(new Scene(root, 640, 460));
            stage.setX(xPos[i]);
            stage.setY(100);
            stage.show();
        }
    }

    private void demo() {  
    	//Adding Departments user accounts for demo
    	
    	departments.add(new Department("ULTRASOUND", "City General Hospital", 35,
    	        Arrays.asList(new TimeSlot("10:00 AM"), new TimeSlot("2:30 PM"))));
    	departments.add(new Department("MRI", "Northside Medical Center", 20,
    	        Arrays.asList(new TimeSlot("11:00 AM"), new TimeSlot("3:00 PM"))));
    	departments.add(new Department("LAB_TEST", "Downtown Clinic", 25,
    	        Arrays.asList(new TimeSlot("8:30 AM"), new TimeSlot("11:30 AM"))));
    	departments.add(new Department("CT_SCAN", "Northside Medical Center", 15,
    	        Arrays.asList(new TimeSlot("11:00 AM"), new TimeSlot("4:00 PM"))));
    	departments.add(new Department("X_RAY", "Downtown Clinic", 10,
    	        Arrays.asList(new TimeSlot("8:00 AM"), new TimeSlot("12:00 PM"), new TimeSlot("3:00 PM"))));
    	
        patientMap.put("P001", new Patient("P001", "John Doe",  "john1234"));
        patientMap.put("P002", new Patient("P002", "Jane Doe",  "jane1234"));
        patientMap.put("P003", new Patient("P003", "Bruno Mars",  "bruno1234"));
        patientMap.put("P004", new Patient("P004", "Iron Man",  "iron1234"));
        patientMap.put("P005", new Patient("P005", "Thor Odinson",  "thor1234"));
        patientMap.put("P006", new Patient("P006", "Boris Mason",  "boris1234"));

        authMap.put("D123", new Doctor("D123", "Dr. Yu", "abcde12345", SubDepartment.RADIOLOGY));
        authMap.put("F123", new FrontDesk("F123", "David Robinson", "qwer1234"));
    }

    public static void main(String[] args) { 
    	launch(args); 
    }
}
