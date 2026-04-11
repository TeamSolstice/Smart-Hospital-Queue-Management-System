package function;

import User.Doctor;
import User.Patient;
import java.time.LocalDateTime;

public class ConsultationRecord {

    private String recordID;
    private Patient patient;
    private Doctor doctor;
    private String chiefComplaint;
    private String diagnosis;
    private LocalDateTime timestamp;

    public ConsultationRecord(String recordID, Patient patient, Doctor doctor,
                               String chiefComplaint, String diagnosis) {
        this.recordID = recordID;
        this.patient = patient;
        this.doctor = doctor;
        this.chiefComplaint = chiefComplaint;
        this.diagnosis = diagnosis;
        this.timestamp = LocalDateTime.now();
    }

    public String getRecordID() { return recordID; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getChiefComplaint() { return chiefComplaint; }
    public String getDiagnosis() { return diagnosis; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Record[" + recordID + "] " + patient.getName() + 
               " - " + chiefComplaint;
    }
}
