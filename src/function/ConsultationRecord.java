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
    private boolean scanRequired;
    private ScanType scanType;
    private Priority priority;
    private LocalDateTime timestamp;

    public ConsultationRecord(String recordID, Patient patient, Doctor doctor,
                               String chiefComplaint, String diagnosis,
                               boolean scanRequired, ScanType scanType, Priority priority) {
        this.recordID = recordID;
        this.patient = patient;
        this.doctor = doctor;
        this.chiefComplaint = chiefComplaint;
        this.diagnosis = diagnosis;
        this.scanRequired = scanRequired;
        this.scanType = scanType;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
    }

    public String getRecordID() { return recordID; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public String getChiefComplaint() { return chiefComplaint; }
    public String getDiagnosis() { return diagnosis; }
    public boolean isScanRequired() { return scanRequired; }
    public ScanType getScanType() { return scanType; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Record[" + recordID + "] " + patient.getName() + 
               " - " + chiefComplaint;
    }
}
