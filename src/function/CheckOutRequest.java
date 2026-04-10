package function;

import java.util.ArrayList;
import User.Patient;
import User.Doctor;

public class CheckOutRequest {
	
	private final Patient patient;
	private final Doctor doctor;
	private Priority priority;
	private String followUpNotes;
	private ArrayList<ScanType> scans;
	private boolean testRequired;
	
	public CheckOutRequest(Patient patient, Doctor doctor) {
		this.patient = patient;
		this.doctor = doctor;
		this.priority = Priority.REGULAR;
		this.followUpNotes = "";
		this.scans = new ArrayList<ScanType>();
		this.testRequired = false;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public void setFollowUpNotes(String notes) {
		this.followUpNotes = notes;
	}
	
	public void addScans(ScanType... scan) {
		for (ScanType scanType : scan) scans.add(scanType);
	}
	
	public void setTestRequirement(boolean requirement) {
		this.testRequired = requirement;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public String getFollowUpNotes() {
		return followUpNotes;
	}
	
	public ArrayList<ScanType> getScans() {
		return scans;
	}
	
	public boolean getTestRequirement() {
		return testRequired;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public Doctor getDoctor() {
		return doctor;
	}
}
