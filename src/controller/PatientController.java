package controller;

import application.Main;
import User.Patient;
import function.Department;
import function.TimeSlot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class PatientController {

    private Patient currentPatient;
    private List<Department> options;
    
    @FXML 
    private Label welcomeLabel;
    @FXML 
    private Label notificationLabel;
    @FXML 
    private Label recommendLabel;
    @FXML 
    private Label confirmationLabel;

    @FXML 
    private Button bookButton;
    @FXML 
    private Button confirmButton;

    @FXML 
    private ListView<String> slotListView;
    @FXML 
    private ListView<String> departmentListView;
    
    public void setPatient(Patient p) {
        this.currentPatient = p;
        initDashboard();
    }

    private void initDashboard() {
        if (welcomeLabel != null)
            welcomeLabel.setText("Welcome, " + currentPatient.getName() + "!");

        if (departmentListView != null) {
            ObservableList<String> rows = FXCollections.observableArrayList(
                    Main.departments.stream().map(Department::toString).collect(Collectors.toList())
            );
            departmentListView.setItems(rows);
        }

        if (notificationLabel != null) {
            String n = currentPatient.getNotification();
            if(n.isEmpty()) 
            	notificationLabel.setText("There is no notifications.");
            else
            	notificationLabel.setText("※ " + n);
        }

        if (bookButton != null) {
            bookButton.setDisable(!currentPatient.hasConsultation());
            if(currentPatient.hasConsultation()) bookButton.setText("Book Appointment ➜");
            else bookButton.setText("Appointment locked — wait for doctor's referral");
        }
    }

    @FXML
    private void handleBookButton() {
        String notification = parseNotificationType(currentPatient.getNotification());
        List<Department> matches 
        	= Main.departments.stream().filter(d -> d.getName().equalsIgnoreCase(notification)).collect(Collectors.toList());
        //Find if notification is matching with departments 
        //This is why notification should be same with name of departments 
        
        if (matches.isEmpty()) {
            notificationLabel.setText("We can't find valid " + notification + " departments");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookAppointment.fxml"));
            Parent root = loader.load();
            PatientController ctrl = loader.getController();
            ctrl.currentPatient = this.currentPatient;
            ctrl.initBooking(matches, notification);
            getStage(welcomeLabel).setScene(new Scene(root, 800, 600));
        } 
        catch (Exception e) { 
        	e.printStackTrace(); 
        }
    }


    public void initBooking(List<Department> dept, String notification) {
        this.options = dept;

        if (recommendLabel != null)
        	recommendLabel.setText("We recommend you to : " + notification);

        if (slotListView != null) {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (Department d : dept)
                for (TimeSlot s : d.getAvailableSlots())
                    if (s.isAvailable())
                        items.add(d.getLocation() + "  |  " + s.getTime() + "  |  ~" + d.getWaitTime() + " min wait");
            slotListView.setItems(items);
        }

        if (confirmButton != null) confirmButton.setDisable(true);

        if (slotListView != null)
            slotListView.getSelectionModel().selectedIndexProperty().addListener(
                    (obs, oldV, newV) -> {if (confirmButton != null) confirmButton.setDisable(newV.intValue() < 0);});
    }

    @FXML
    private void handleConfirm() {
        
    	int cnt = 0;
    	int i = slotListView.getSelectionModel().getSelectedIndex();
        if (i < 0 || options == null) return;

        for (Department d : options) {
            for (TimeSlot s : d.getAvailableSlots()) {
                if (!s.isAvailable()) continue;
                if (cnt == i) {
                    currentPatient.bookAppointment(d, s);
                    if (confirmationLabel != null)
                        confirmationLabel.setText("Book Confirmed: " + s.getTime() + ", " + d.getLocation());
                    if (confirmButton != null) confirmButton.setDisable(true);
                    initBooking(options, options.get(0).getName());
                    return;
                }
                cnt++;
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader FL = new FXMLLoader(getClass().getResource("/PatientDashboard.fxml"));
            Parent root = FL.load();
            PatientController ctrl = FL.getController();
            ctrl.setPatient(currentPatient);
            getStage(slotListView).setScene(new Scene(root, 800, 600));
        } 
        catch (Exception e) { 
        	e.printStackTrace(); 
        }
    }

    private String parseNotificationType(String notification) {
        for (String t : new String[]{"A", "B", "C", "D", "E"})
            if (notification.toUpperCase().contains(t.toUpperCase())) return t;
        return "Default";
    }//Should be changed depends on what treatment cases are we using

    private Stage getStage(javafx.scene.Node node) {
        return (Stage) node.getScene().getWindow();
    }
}