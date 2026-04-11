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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hospital Queue Management");
        primaryStage.setScene(new Scene(root, 640, 460));
        primaryStage.show();
    }

    private void demo() {  
    	//Adding Departments user accounts for demo
    	
        departments.add(new Department("Dept_1", "A", 30, Arrays.asList(new TimeSlot("12:00 AM"), new TimeSlot("12:30 AM"), new TimeSlot("1:00 PM"))));
        departments.add(new Department("Dept_2", "B", 20, Arrays.asList(new TimeSlot("13:00 AM"), new TimeSlot("3:00 PM"))));
        departments.add(new Department("Dept_3", "C", 40,  Arrays.asList(new TimeSlot("11:30 AM"), new TimeSlot("2:00 PM"))));

        patientMap.put("P001", new Patient("P001", "John Doe"));
        patientMap.put("P002", new Patient("P002", "Jane Doe"));

        // After Person 2 done,
        // authMap.put("D123", new Doctor("D123", "Dr. Yu", "abcde12345", SubDepartment.RADIOLOGY));

        //After Person 3 done,
        // authMap.put("F123", new FrontDesk("F123", "David Robinson", "qwer1234"));
        authMap.put("D123", new Doctor("D123", "Dr. Yu", "abcde12345", SubDepartment.RADIOLOGY));
        authMap.put("F123", new FrontDesk("F123", "David Robinson", "qwer1234"));
    }

    public static void main(String[] args) { 
    	launch(args); 
    }
}