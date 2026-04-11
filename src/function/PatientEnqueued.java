package function;

public class PatientEnqueued {

    private String patientName;
    private String priority;

    public PatientEnqueued(String patientName, String priority) {
        this.patientName = patientName;
        this.priority = priority;
    }

    public String getPatientName() { return patientName; }
    public String getPriority() { return priority; }

    @Override
    public String toString() {
        return patientName + " [" + priority + "]";
    }
}
