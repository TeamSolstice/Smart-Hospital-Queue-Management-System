package controller;

import User.Doctor;
import User.Patient;
import application.Main;
import function.CheckOutRequest;
import function.ConsultationRecord;
import function.PatientEnqueued;
import function.Priority;
import function.ScanType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DoctorController {

    // Header label in the top bar that shows the logged-in doctor's name.
    @FXML private Label doctorNameLabel;

    // Left-side patient list.
    @FXML private ListView<String> patientListView;

    // Left-side label under the patient list.
    @FXML private Label selectedPatientLabel;

    // "Begin Consultation" button under the patient list.
    @FXML private Button beginConsultationButton;

    // Main tab container for "Consultation" and "Wrap Up".
    @FXML private TabPane doctorTabPane;

    // First tab: "Consultation".
    @FXML private Tab consultationTab;

    // Second tab: "Wrap Up".
    @FXML private Tab wrapUpTab;

    // Consultation tab: "Chief Complaint" text area.
    @FXML private TextArea chiefComplaintArea;

    // Consultation tab: "Diagnosis" text field.
    @FXML private TextField diagnosisField;

    // Consultation tab: "Notes" text area.
    @FXML private TextArea notesArea;

    // Wrap-up tab label that shows the active patient.
    @FXML private Label wrapUpPatientLabel;

    // Wrap-up tab combo box: "Scheduling Priority".
    @FXML private ComboBox<String> priorityCombo;

    // Wrap-up tab combo box: "Test Required".
    @FXML private ComboBox<String> testRequiredCombo;

    // Wrap-up tab toggle button: "ULTRASOUND".
    @FXML private ToggleButton ultrasoundToggle;

    // Wrap-up tab toggle button: "MRI".
    @FXML private ToggleButton mriToggle;

    // Wrap-up tab toggle button: "CT SCAN".
    @FXML private ToggleButton ctScanToggle;

    // Wrap-up tab toggle button: "X-RAY".
    @FXML private ToggleButton xrayToggle;

    // Wrap-up tab toggle button: "LAB".
    @FXML private ToggleButton labToggle;

    // Wrap-up tab: "Follow Up Instructions" text area.
    @FXML private TextArea followUpInstructionsArea;

    // Wrap-up tab button: "Send to Front Desk".
    @FXML private Button sendToFrontDeskButton;

    // Optional status label for validation / success messages.
    @FXML private Label statusLabel;

    private Doctor currentDoctor;
    private Patient selectedPatient;
    private ConsultationRecord latestConsultationRecord;
    private CheckOutRequest latestCheckOutRequest;

    // Called automatically by FXMLLoader after all fx:id fields are injected.
    // Sets up combo box choices, patient list listeners, and the initial disabled state.
    @FXML
    public void initialize() {
        if (priorityCombo != null) {
            priorityCombo.getItems().setAll("Regular", "Emergency");
            priorityCombo.setValue("Regular");
        }

        if (testRequiredCombo != null) {
            testRequiredCombo.getItems().setAll("True", "False");
            testRequiredCombo.setValue("False");
            testRequiredCombo.valueProperty().addListener((obs, oldValue, newValue) -> updateTestControls());
        }

        if (patientListView != null) {
            patientListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> handlePatientSelection()
            );
        }

        loadPatients();
        setConsultationEnabled(false);
        updateSelectedPatientLabels(null);
        setStatus("Select a patient to begin consultation.");
    }

    public void setDoctor(Doctor doctor) {
        this.currentDoctor = doctor;
        if (doctorNameLabel != null && doctor != null) {
            doctorNameLabel.setText(doctor.getName());
        }
    }

    public ConsultationRecord getLatestConsultationRecord() {
        return latestConsultationRecord;
    }

    public CheckOutRequest getLatestCheckOutRequest() {
        return latestCheckOutRequest;
    }

    // Link this to the patient ListView selection change.
    // SceneBuilder can also call this with onMouseClicked if you want manual selection handling.
    @FXML
    private void handlePatientSelection() {
        if (patientListView == null) {
            return;
        }

        String selectedRow = patientListView.getSelectionModel().getSelectedItem();
        if (selectedRow == null || selectedRow.isBlank()) {
            selectedPatient = null;
            updateSelectedPatientLabels(null);
            if (beginConsultationButton != null) {
                beginConsultationButton.setDisable(true);
            }
            return;
        }

        String patientId = selectedRow.split(" - ", 2)[0].trim();
        selectedPatient = Main.patientMap.get(patientId);
        updateSelectedPatientLabels(selectedPatient);

        if (beginConsultationButton != null) {
            beginConsultationButton.setDisable(selectedPatient == null);
        }
    }

    // Link this to the "Begin Consultation" button onAction.
    // Enables the consultation inputs and unlocks the wrap-up tab.
    @FXML
    private void handleBeginConsultation() {
        if (selectedPatient == null) {
            setStatus("Select a patient first.");
            return;
        }

        setConsultationEnabled(true);
        updateSelectedPatientLabels(selectedPatient);

        if (doctorTabPane != null && consultationTab != null) {
            doctorTabPane.getSelectionModel().select(consultationTab);
        }

        if (priorityCombo != null && priorityCombo.getValue() == null) {
            priorityCombo.setValue("Regular");
        }

        if (testRequiredCombo != null && testRequiredCombo.getValue() == null) {
            testRequiredCombo.setValue("False");
        }

        updateTestControls();
        setStatus("Consultation started for " + selectedPatient.getName() + ".");
    }

    // Link this to the "Test Required" combo box onAction.
    // Enables or disables the scan toggle buttons based on True / False.
    @FXML
    private void handleTestRequirementChange() {
        updateTestControls();
    }

    // Link this to the "Send to Front Desk" button onAction.
    // Builds the ConsultationRecord and CheckOutRequest from the current form inputs.
    @FXML
    private void handleSendToFrontDesk() {
        if (selectedPatient == null) {
            setStatus("Select a patient before sending to front desk.");
            return;
        }

        String complaint = getText(chiefComplaintArea);
        String diagnosis = getText(diagnosisField);
        String notes = getText(notesArea);
        String followUp = getText(followUpInstructionsArea);

        if (complaint.isBlank()) {
            setStatus("Chief complaint is required.");
            return;
        }

        if (diagnosis.isBlank()) {
            setStatus("Diagnosis is required.");
            return;
        }

        Priority priority = Priority.REGULAR;
        if (priorityCombo != null && priorityCombo.getValue() != null) {
            priority = parsePriority(priorityCombo.getValue());
        }

        boolean testRequired = testRequiredCombo != null
            && Boolean.parseBoolean(testRequiredCombo.getValue());

        List<ScanType> selectedScans = getSelectedScans();
        if (testRequired && selectedScans.isEmpty()) {
            setStatus("Select at least one test when tests are required.");
            return;
        }

        latestConsultationRecord = new ConsultationRecord(
            "REC-" + System.currentTimeMillis(),
            selectedPatient,
            currentDoctor,
            complaint,
            diagnosis
        );
        Main.consultationRecords.add(latestConsultationRecord);

        latestCheckOutRequest = new CheckOutRequest(selectedPatient, currentDoctor);
        latestCheckOutRequest.setPriority(priority);
        latestCheckOutRequest.setTestRequirement(testRequired);
        latestCheckOutRequest.setFollowUpNotes(buildFollowUpNotes(notes, followUp));
        if (!selectedScans.isEmpty()) {
            latestCheckOutRequest.addScans(selectedScans.toArray(new ScanType[0]));
        }

        selectedPatient.setHasConsultation(true);
        selectedPatient.setNotification(buildNotification(testRequired, selectedScans, followUp));

        if (testRequired) {
            Main.scanQueue.add(new PatientEnqueued(selectedPatient, priority));
        }

        setStatus("Consultation saved for " + selectedPatient.getName() + ".");
        resetDashboardState();
    }

    private void loadPatients() {
        if (patientListView == null) {
            return;
        }

        patientListView.getItems().clear();
        Main.patientMap.values().stream()
            .filter(p -> !p.hasConsultation()) //filter patient
            .sorted(Comparator.comparing(Patient::getID))
            .forEach(patient ->
                patientListView.getItems().add(patient.getID() + " - " + patient.getName())
            );

        if (beginConsultationButton != null) {
            beginConsultationButton.setDisable(true);
        }
    }

    // Central enable/disable helper for the controls that should stay locked
    // until the doctor clicks "Begin Consultation".
    private void setConsultationEnabled(boolean enabled) {
        if (chiefComplaintArea != null) {
            chiefComplaintArea.setDisable(!enabled);
        }
        if (diagnosisField != null) {
            diagnosisField.setDisable(!enabled);
        }
        if (notesArea != null) {
            notesArea.setDisable(!enabled);
        }
        if (wrapUpTab != null) {
            wrapUpTab.setDisable(!enabled);
        }
        if (priorityCombo != null) {
            priorityCombo.setDisable(!enabled);
        }
        if (testRequiredCombo != null) {
            testRequiredCombo.setDisable(!enabled);
        }
        if (followUpInstructionsArea != null) {
            followUpInstructionsArea.setDisable(!enabled);
        }
        if (sendToFrontDeskButton != null) {
            sendToFrontDeskButton.setDisable(!enabled);
        }

        if (!enabled && doctorTabPane != null && consultationTab != null) {
            doctorTabPane.getSelectionModel().select(consultationTab);
        }

        updateTestControls();
    }

    // Enables the scan ToggleButtons only when consultation is active
    // and the "Test Required" combo box is set to True.
    private void updateTestControls() {
        boolean consultationEnabled = chiefComplaintArea != null && !chiefComplaintArea.isDisable();
        boolean testRequired = testRequiredCombo != null
            && Boolean.parseBoolean(testRequiredCombo.getValue());
        boolean enableButtons = consultationEnabled && testRequired;

        setToggleState(ultrasoundToggle, enableButtons);
        setToggleState(mriToggle, enableButtons);
        setToggleState(ctScanToggle, enableButtons);
        setToggleState(xrayToggle, enableButtons);
        setToggleState(labToggle, enableButtons);
    }

    // Shared helper for scan ToggleButtons.
    private void setToggleState(ToggleButton button, boolean enabled) {
        if (button == null) {
            return;
        }

        button.setDisable(!enabled);
        if (!enabled) {
            button.setSelected(false);
        }
    }

    // Reads which scan/test toggle buttons are selected in the wrap-up tab.
    private List<ScanType> getSelectedScans() {
        List<ScanType> scans = new ArrayList<>();

        if (ultrasoundToggle != null && ultrasoundToggle.isSelected()) {
            scans.add(ScanType.ULTRASOUND);
        }
        if (mriToggle != null && mriToggle.isSelected()) {
            scans.add(ScanType.MRI);
        }
        if (ctScanToggle != null && ctScanToggle.isSelected()) {
            scans.add(ScanType.CT_SCAN);
        }
        if (xrayToggle != null && xrayToggle.isSelected()) {
            scans.add(ScanType.X_RAY);
        }
        if (labToggle != null && labToggle.isSelected()) {
            scans.add(ScanType.LAB_TEST);
        }

        return scans;
    }

    // Updates both patient labels:
    // 1. left-side "Selected Patient"
    // 2. wrap-up tab "Patient"
    private void updateSelectedPatientLabels(Patient patient) {
        String selectedText = patient == null
            ? "Selected Patient: None"
            : "Selected Patient: " + patient.getName();
        String wrapUpText = patient == null
            ? "Patient: None"
            : "Patient: " + patient.getName();

        if (selectedPatientLabel != null) {
            selectedPatientLabel.setText(selectedText);
        }
        if (wrapUpPatientLabel != null) {
            wrapUpPatientLabel.setText(wrapUpText);
        }
    }

    // Clears the form and returns the dashboard to its initial locked state
    // after a consultation has been submitted.
    private void resetDashboardState() {
        selectedPatient = null;

        if (patientListView != null) {
            patientListView.getSelectionModel().clearSelection();
        }

        clearInput(chiefComplaintArea);
        clearInput(diagnosisField);
        clearInput(notesArea);
        clearInput(followUpInstructionsArea);

        if (priorityCombo != null) {
            priorityCombo.setValue("Regular");
        }
        if (testRequiredCombo != null) {
            testRequiredCombo.setValue("False");
        }

        updateSelectedPatientLabels(null);
        setConsultationEnabled(false);
        loadPatients();
    }

    // Clears a TextArea control.
    private void clearInput(TextArea field) {
        if (field != null) {
            field.clear();
        }
    }

    // Clears a TextField control.
    private void clearInput(TextField field) {
        if (field != null) {
            field.clear();
        }
    }

    // Safely reads trimmed text from a TextArea.
    private String getText(TextArea field) {
        return field == null ? "" : field.getText().trim();
    }

    // Safely reads trimmed text from a TextField.
    private String getText(TextField field) {
        return field == null ? "" : field.getText().trim();
    }

    // Combines consultation notes and follow-up instructions into the
    // single notes string stored in CheckOutRequest.
    private String buildFollowUpNotes(String notes, String followUp) {
        if (notes.isBlank()) {
            return followUp;
        }
        if (followUp.isBlank()) {
            return notes;
        }
        return notes + System.lineSeparator() + System.lineSeparator() + followUp;
    }

    // Builds the notification text later shown to the patient.
    private String buildNotification(boolean testRequired, List<ScanType> scans, String followUp) {
        if (!testRequired) {
            return followUp.isBlank() ? "Consultation completed. No tests required." : followUp;
        }

        String scanSummary = scans.isEmpty()
            ? "Tests ordered"
            : "Tests ordered: " + scans;

        if (followUp.isBlank()) {
            return scanSummary;
        }
        return scanSummary + " | Follow-up: " + followUp;
    }

    // Maps the user-facing priority combo box values to the Priority enum.
    private Priority parsePriority(String priorityValue) {
        if (priorityValue == null) {
            return Priority.REGULAR;
        }
        if ("Emergency".equalsIgnoreCase(priorityValue)) {
            return Priority.EMERGENCY;
        }
        return Priority.REGULAR;
    }

    // Writes validation / result messages into the status label.
    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}
