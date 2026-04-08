package User;

import function.Appointment;
import function.AppointmentStatus;
import function.Department;
import function.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
	
    private boolean hasConsultation;
    private String notification; //*notification should be same with name of departments 
    							 //-> read Patientcontroller class's handleBookButton method for detail
    private List<Appointment> appointments; //List of appointment of such patient
    //Probably should be implemented with queue, but I suppose order doesn't matter

    public Patient(String id, String name) { //Constructor for patient
        super(id, name);
        this.hasConsultation = false;
        this.notification = "";
        this.appointments = new ArrayList<>();
    }
    
    @Override
    public String getID(){ 
    	return id; 
    }
    
    @Override
    public String getName(){ 
    	return name; 
    }
    
    public void setHasConsultation(boolean consultantion){ 
    	this.hasConsultation = consultantion;
    }
    
    public boolean hasConsultation(){ //Setter for this should be implemented in Doctor class
    	return hasConsultation; 
    }
    
    public void setNotification(String notification){
    	this.notification = notification; 
    }
    
    public String getNotification(){ 
    	return notification; 
    }

    public List<Appointment> getAppointments() { 
    	return appointments; 
    }    
    
    public void bookAppointment(Department dept, TimeSlot slot) {
    	
        if (!hasConsultation) throw new IllegalStateException("No Doctor's referral found. Cannot book this time."); 
        //If patient doesn't have consultation, throw exception
        appointments.add(new Appointment(id, dept, slot.getTime(), AppointmentStatus.CONFIRMED));
        //Add to appointment list 
        slot.setAvailable(false);
        //Make time slot not available
    }    
    
    @Override
    public String toString() { 
    	return name + ": [" + id + "]"; 
    }
}