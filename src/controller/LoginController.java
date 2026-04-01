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
    private TextField idField;
    @FXML 
    private PasswordField passwordField;
    @FXML 
    private Label errorLabel;

    @FXML
    private void handlePatientButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientDashboard.fxml"));
            Parent root = loader.load();
            PatientController pc = loader.getController();
            pc.setPatient(new Patient("GUEST", "Guest Patient"));
            getStage().setScene(new Scene(root, 800, 600));
        } 
        catch (Exception e) {
            errorLabel.setText("Error loading patient dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        try {
            Staff st = authenticate(idField.getText().trim(), passwordField.getText().trim());
            st.viewDashboard(getStage());
        } 
        catch (InvalidLoginException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private Staff authenticate(String id, String pw) throws InvalidLoginException { //simple auth logic
        if (id.isEmpty() || pw.isEmpty())
            throw new InvalidLoginException("Please type both ID and password");
        
        Staff st = Main.authMap.get(id);
        
        if (st == null)
            throw new InvalidLoginException("Id does not exist.");
        else if (!st.getPassword().equals(pw))
            throw new InvalidLoginException("ID and Password doesn not match");
        return st;
    }

    private Stage getStage() {
        return (Stage) idField.getScene().getWindow();
    }
}