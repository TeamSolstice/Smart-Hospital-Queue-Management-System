package function;

import User.Patient;
import java.time.LocalTime;

public class PatientEnqueued {
	
	private final Patient patient;
	private Priority priority;
	private final LocalTime timeQueued;
	
	public PatientEnqueued(Patient patient, Priority priority) {
		this.patient = patient;
		this.priority = priority;
		this.timeQueued = LocalTime.now();
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	// helper method in getting the level code (corresponding integers) 
	// from the priority enum's assigned priority number
	public int getPriorityLevelCode() {
		return priority.getPriorityLevel();
	}
	
	public LocalTime getTimeQueued() {
		return timeQueued;
	}
	
	@Override
	public String toString() {
		return priority + " patient " + patient.toString() + " queued at: " + timeQueued;
	}
}
