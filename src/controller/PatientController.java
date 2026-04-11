package controller;

import application.Main;
import User.Patient;
import function.Appointment;
import function.AppointmentStatus;
import function.Department;
import function.TimeSlot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PatientController {

    private Patient currentPatient;
    private List<Department> options;
    private String currentNotification;

    @FXML private Label welcomeLabel;
    @FXML private Label notificationLabel;
    @FXML private Label appointmentStatusLabel;
    @FXML private Button bookButton;
    @FXML private ListView<String> departmentListView;
    @FXML private ListView<String> appointmentListView;

    @FXML private Label recommendLabel;
    @FXML private Label confirmationLabel;
    @FXML private Button confirmButton;
    @FXML private ListView<String> slotListView;


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
            notificationLabel.setText(n.isEmpty() ? "There is no notifications." : "※ " + n);
        }

        if (bookButton != null) {
            bookButton.setDisable(!currentPatient.hasConsultation());
            bookButton.setText(currentPatient.hasConsultation()
                    ? "Book Appointment ➜"
                    : "Appointment locked — wait for doctor's referral");
        }

        loadAppointments();
    }

    private void loadAppointments() {
        if (appointmentListView == null) return;
        ObservableList<String> items = FXCollections.observableArrayList();
        List<Appointment> appts = currentPatient.getAppointments();

        appts.sort(Comparator.comparing(Appointment::getScheduledTime));

        if (appts.isEmpty()) {
            items.add("No appointments yet.");
        } else {
            for (int i = 0; i < appts.size(); i++)
                items.add((i + 1) + ". " + appts.get(i).toString());
        }
        appointmentListView.setItems(items);
    }



    @FXML
    private void handleRefresh() {
        initDashboard();
        if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Refreshed.");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientDashboard.fxml"));
            Parent root = loader.load();
            PatientController ctrl = loader.getController();
            ctrl.setPatient(currentPatient);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBookButton() {
        String scanType = parseScanType(currentPatient.getNotification());
        if (scanType == null) {
            notificationLabel.setText("No valid scan type found in notification.");
            return;
        }

        List<Department> matches = Main.departments.stream()
                .filter(d -> d.getName().equalsIgnoreCase(scanType))  // location → name
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            notificationLabel.setText("No departments found for: " + scanType);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BookAppointment.fxml"));
            Parent root = loader.load();
            PatientController ctrl = loader.getController();
            ctrl.currentPatient = this.currentPatient;
            ctrl.initBooking(matches, scanType);
            getStage(welcomeLabel).setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleMarkCompleted() {
        int i = appointmentListView.getSelectionModel().getSelectedIndex();
        List<Appointment> appts = currentPatient.getAppointments();
        if (i < 0 || i >= appts.size()) {
            if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Please select an appointment.");
            return;
        }
        Appointment appt = appts.get(i);
        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            appointmentStatusLabel.setText("Already completed.");
            return;
        }
        appt.setStatus(AppointmentStatus.COMPLETED);
        if (appointmentStatusLabel != null)
            appointmentStatusLabel.setText("Marked as completed: " + appt.getScheduledTime());
        loadAppointments();
    }

    public void initBooking(List<Department> dept, String notification) {
        this.options = dept;
        this.currentNotification = notification;

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
                    (obs, oldV, newV) -> { if (confirmButton != null) confirmButton.setDisable(newV.intValue() < 0); });
    }
    @FXML
    private void handleRefreshBooking() {
        if (options != null && currentNotification != null)
            initBooking(options, currentNotification);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientDashboard.fxml"));
            Parent root = loader.load();
            PatientController ctrl = loader.getController();
            ctrl.setPatient(currentPatient);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        confirmationLabel.setText("Confirmed: " + s.getTime() + ", " + d.getLocation());
                    if (confirmButton != null) confirmButton.setDisable(true);
                    initBooking(options, currentNotification);
                    return;
                }
                cnt++;
            }
        }
    }
    
    @FXML
    private void handleCancelAppointment() {
        int i = appointmentListView.getSelectionModel().getSelectedIndex();
        List<Appointment> appts = currentPatient.getAppointments();
        appts.sort(Comparator.comparing(Appointment::getScheduledTime));

        if (i < 0 || i >= appts.size()) {
            if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Please select an appointment.");
            return;
        }
        Appointment appt = appts.get(i);
        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Completed appointments cannot be cancelled.");
            return;
        }
        if (appt.getStatus() == AppointmentStatus.CANCELLED) {
            if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Already cancelled.");
            return;
        }
        appt.setStatus(AppointmentStatus.CANCELLED);
        for (Department d : Main.departments)
            for (TimeSlot s : d.getAvailableSlots())
                if (s.getTime().equals(appt.getScheduledTime()) && !s.isAvailable())
                    s.setAvailable(true);
        if (appointmentStatusLabel != null)
            appointmentStatusLabel.setText("Cancelled: " + appt.getScheduledTime());
        loadAppointments();
    }

    @FXML
    private void handleRemoveAppointment() {
        int i = appointmentListView.getSelectionModel().getSelectedIndex();
        List<Appointment> appts = currentPatient.getAppointments();
        appts.sort(Comparator.comparing(Appointment::getScheduledTime));

        if (i < 0 || i >= appts.size()) {
            if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Please select an appointment.");
            return;
        }
        Appointment appt = appts.get(i);
        if (appt.getStatus() == AppointmentStatus.CONFIRMED || appt.getStatus() == AppointmentStatus.PENDING) {
            if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Only completed or cancelled appointments can be removed.");
            return;
        }
        appts.remove(i);
        if (appointmentStatusLabel != null) appointmentStatusLabel.setText("Removed.");
        loadAppointments();
    }    
    
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 640, 460));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    private String parseScanType(String notification) {
        int start = notification.indexOf("[");
        int end = notification.indexOf("]");
        if (start != -1 && end != -1)
            return notification.substring(start + 1, end);
        return null;
    }    
    
    private Stage getStage(javafx.scene.Node node) {
        return (Stage) node.getScene().getWindow();
    }
}
