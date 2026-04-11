package function;

public class Appointment {
	
    private String patientID;
    private Department department;
    private String scheduledTime;
    private AppointmentStatus status;

    public Appointment(String patientID, Department department, String scheduledTime, AppointmentStatus status) {
        this.patientID = patientID;
        this.department = department;
        this.scheduledTime = scheduledTime;
        this.status = status;
    }

    public String getPatientID() { 
    	return patientID; 
    }
    
    public Department getDepartment() { 
    	return department; 
    }
    
    public String getScheduledTime() { 
    	return scheduledTime; 
    }
    
    public void setStatus(AppointmentStatus status) { 
    	this.status = status; 
    }
    
    public AppointmentStatus getStatus() { 
    	return status; 
    }

    @Override
    public String toString() {
        return department.getName() + " @ " + scheduledTime + " — " + status;
    }
}