package controller;

import User.FrontDesk;
import User.Patient;
import function.PatientEnqueued;
import function.TimeSlot;
import function.Appointment;
import function.AppointmentStatus;
import function.Department;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

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
        int count = 0;
        Iterator<PatientEnqueued> iterator = Main.scanQueue.getIterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        statusLabel.setText("Pending requests: " + count);
        PriorityQueue<PatientEnqueued> temp = new PriorityQueue<>(Main.scanQueue.getQueue());
        while (!temp.isEmpty()) {
            requestListView.getItems().add(temp.poll().toString());
        }
        //Modified into priority queue
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
    public void handleBookAppointment() {
        String selected = requestListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a request first.");
            return;
        }

        PatientEnqueued next = Main.scanQueue.dequeue();
        if (next == null) {
            statusLabel.setText("Queue is empty.");
            return;
        }
        
        //Implemented appointment handler 
        Patient patient = next.getPatient();
        List<String> scanTypes = parseScanTypes(patient.getNotification());

        boolean booked = false;
        for (String scanType : scanTypes) {
            for (Department dept : Main.departments) {
                if (!dept.getName().equalsIgnoreCase(scanType)) continue;
                for (TimeSlot slot : dept.getAvailableSlots()) {
                    if (!slot.isAvailable()) continue;
                    patient.getAppointments().add(new Appointment(patient.getID(), dept, slot.getTime(), AppointmentStatus.CONFIRMED));
                    slot.setAvailable(false);
                    statusLabel.setText("Booked: " + patient.getName() + " → " + dept.getName()+ " @ " + slot.getTime()+ " (" + dept.getLocation() + ")");
                    booked = true;
                    break;
                }
                if (booked) break;
            }
            if (booked) break;
        }

        if (!booked)
            statusLabel.setText("No available slots found for " + patient.getName() + ".");

        loadRequests();
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
    
    //added helper
    private List<String> parseScanTypes(String notification) {
        List<String> result = new ArrayList<>();
        int start = notification.indexOf("[");
        int end = notification.indexOf("]");
        if (start == -1 || end == -1) return result;
        for (String s : notification.substring(start + 1, end).split(","))
            result.add(s.trim());
        return result;
    }
}
