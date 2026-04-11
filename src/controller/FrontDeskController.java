package controller;

import User.FrontDesk;
import function.PatientEnqueued;
import function.ConsultationRecord;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Iterator;

public class FrontDeskController {

    @FXML private Label staffNameLabel;
    @FXML private ListView<String> requestListView;
    @FXML private ListView<String> departmentListView;
    @FXML private Label statusLabel;

    private FrontDesk currentStaff;

    @FXML
    public void initialize() {
        loadRequests();
        loadDepartments();
    }

    public void setFrontDesk(FrontDesk frontDesk) {
        this.currentStaff = frontDesk;
        staffNameLabel.setText("Staff: " + frontDesk.getName());
    }

    private void loadRequests() {
        requestListView.getItems().clear();
        Iterator<PatientEnqueued> iterator = Main.scanQueue.getIterator();
        while (iterator.hasNext()) {
            PatientEnqueued p = iterator.next();
            requestListView.getItems().add(p.toString());
        }
    }

    private void loadDepartments() {
        departmentListView.getItems().clear();
        for (function.Department dept : Main.departments) {
            departmentListView.getItems().add(
                dept.getName() + " - Wait: " + dept.getWaitTime() + " mins"
            );
        }
    }

    @FXML
    public void handleRefresh() {
        loadRequests();
        loadDepartments();
        statusLabel.setText("Refreshed successfully.");
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) staffNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 640, 460));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBookAppointment() {
        String selected = requestListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a request first.");
            return;
        }
        PatientEnqueued next = Main.scanQueue.dequeue();
        if (next != null) {
            statusLabel.setText("Appointment booked for: " + next.getPatientName());
            loadRequests();
        }
    }

    @FXML
    public void handleNotifyPatient() {
        String selected = requestListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a request first.");
            return;
        }
        statusLabel.setText("Notification sent to patient.");
    }
}
