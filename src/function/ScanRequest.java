package function;

import User.Patient;

public class ScanRequest implements Comparable<ScanRequest> {

    private String requestID;
    private Patient patient;
    private ScanType scanType;
    private Priority priority;
    private boolean isScheduled;

    public ScanRequest(String requestID, Patient patient, 
                       ScanType scanType, Priority priority) {
        this.requestID = requestID;
        this.patient = patient;
        this.scanType = scanType;
        this.priority = priority;
        this.isScheduled = false;
    }

    public String getRequestID() { return requestID; }
    public Patient getPatient() { return patient; }
    public ScanType getScanType() { return scanType; }
    public Priority getPriority() { return priority; }
    public boolean isScheduled() { return isScheduled; }
    public void setScheduled(boolean scheduled) { isScheduled = scheduled; }

    @Override
    public int compareTo(ScanRequest other) {
        if (this.priority == Priority.EMERGENCY && 
            other.priority == Priority.REGULAR) {
            return -1;
        } else if (this.priority == Priority.REGULAR && 
                   other.priority == Priority.EMERGENCY) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "ScanRequest[" + requestID + "] " + 
               patient.getName() + " - " + scanType + 
               " (" + priority + ")";
    }
}