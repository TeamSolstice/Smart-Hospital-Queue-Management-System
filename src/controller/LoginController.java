package controller;

import application.Main;
import handler.InvalidLoginException;
import User.Staff;
import User.Patient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML 
    private TextField     patientIdField;
    @FXML 
    private PasswordField patientPasswordField;
    @FXML 
    private TextField     idField;
    @FXML 
    private PasswordField passwordField;
    @FXML 
    private Label         errorLabel;

    @FXML
    private void handlePatientLogin() {
        try {
            Patient patient = authenticatePatient(
                patientIdField.getText().trim(),
                patientPasswordField.getText().trim()
            );
            loadPatientDashboard(patient);
        } catch (InvalidLoginException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        try {
            Staff st = authenticateStaff(
                idField.getText().trim(),
                passwordField.getText().trim()
            );
            st.viewDashboard(getStage());
        } catch (InvalidLoginException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private Patient authenticatePatient(String id, String pw) throws InvalidLoginException {
        if (id.isEmpty() || pw.isEmpty())
            throw new InvalidLoginException("Please enter both Patient ID and password.");
        Patient patient = Main.patientMap.get(id);
        if (patient == null)
            throw new InvalidLoginException("Patient ID not found: " + id);
        if (!patient.getPassword().equals(pw))
            throw new InvalidLoginException("ID and Password does not match.");
        return patient;
    }

    private Staff authenticateStaff(String id, String pw) throws InvalidLoginException {
        if (id.isEmpty() || pw.isEmpty())
            throw new InvalidLoginException("Please type both ID and password.");
        Staff st = Main.authMap.get(id);
        if (st == null)
            throw new InvalidLoginException("Staff ID does not exist.");
        if (!st.getPassword().equals(pw))
            throw new InvalidLoginException("ID and Password does not match.");
        return st;
    }

    private void loadPatientDashboard(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientDashboard.fxml"));
            Parent root = loader.load();
            PatientController pc = loader.getController();
            pc.setPatient(patient);
            getStage().setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            errorLabel.setText("Error loading patient dashboard.");
            e.printStackTrace();
        }
    }

    private Stage getStage() {
        return (Stage) idField.getScene().getWindow();
    }
}
