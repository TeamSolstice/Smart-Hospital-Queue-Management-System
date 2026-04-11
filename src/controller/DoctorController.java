package controller;

import User.Doctor;
import User.Patient;
import function.ConsultationRecord;
import function.ScanRequest;
import function.ScanType;
import function.Priority;
import function.PatientEnqueued;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class DoctorController {

    @FXML private Label doctorNameLabel;
    @FXML private ListView<String> patientListView;
    @FXML private TextField complaintField;
    @FXML private TextField diagnosisField;
    @FXML private ComboBox<String> scanTypeCombo;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private Label statusLabel;

    private Doctor currentDoctor;
    private Patient selectedPatient;

    @FXML
    public void initialize() {
        scanTypeCombo.getItems().addAll("MRI", "CT_SCAN", "X_RAY", "ULTRASOUND", "LAB_TEST");
        priorityCombo.getItems().addAll("EMERGENCY", "REGULAR");
        loadPatients();
    }

    public void setDoctor(Doctor doctor) {
        this.currentDoctor = doctor;
        doctorNameLabel.setText("Dr. " + doctor.getName());
    }

    private void loadPatients() {
        patientListView.getItems().clear();
        for (String id : Main.patientMap.keySet()) {
            Patient p = Main.patientMap.get(id);
            patientListView.getItems().add(p.getPatientID() + " - " + p.getName());
        }
    }

    @FXML
    public void handleSelectPatient() {
        String selected = patientListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String patientID = selected.split(" - ")[0];
            selectedPatient = Main.patientMap.get(patientID);
            statusLabel.setText("Selected: " + selectedPatient.getName());
        }
    }

    @FXML
    public void handleSendToFrontDesk() {
        if (selectedPatient == null) {
            statusLabel.setText("Please select a patient first.");
            return;
        }
        if (complaintField.getText().isEmpty() || diagnosisField.getText().isEmpty()) {
            statusLabel.setText("Please fill in complaint and diagnosis.");
            return;
        }

        ScanType scanType = ScanType.valueOf(scanTypeCombo.getValue());
        Priority priority = Priority.valueOf(priorityCombo.getValue());

        ConsultationRecord record = new ConsultationRecord(
            "REC" + System.currentTimeMillis(),
            selectedPatient, currentDoctor,
            complaintField.getText(), diagnosisField.getText(),
            true, scanType, priority
        );

        ScanRequest request = new ScanRequest(
            "REQ" + System.currentTimeMillis(),
            selectedPatient, scanType, priority
        );

        PatientEnqueued enqueued = new PatientEnqueued(
            selectedPatient.getName(), priority.toString()
        );

        Main.scanQueue.enqueue(enqueued);
        Main.consultationRecords.add(record);
        selectedPatient.setHasConsultation(true);

        statusLabel.setText("Sent to Front Desk: " + selectedPatient.getName() + 
                           " needs " + scanType);
    }

    @FXML
    public void handleClosePatient() {
        if (selectedPatient == null) {
            statusLabel.setText("Please select a patient first.");
            return;
        }
        statusLabel.setText("Closed case for: " + selectedPatient.getName());
        selectedPatient = null;
        complaintField.clear();
        diagnosisField.clear();
    }
}