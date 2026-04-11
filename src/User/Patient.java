package User;

import function.Appointment;
import function.AppointmentStatus;
import function.Department;
import function.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String patientID;
    private String name;
    private String password;

    private boolean hasConsultation;
    private String notification;
    private List<Appointment> appointments;

    public Patient(String patientID, String name, String password) {
        this.patientID = patientID;
        this.name = name;
        this.password = password;
        this.hasConsultation = false;
        this.notification = "";
        this.appointments = new ArrayList<>();
    }

    public String getPatientID()  { return patientID; }
    public String getName()       { return name; }
    public String getPassword()   { return password; }

    public void setHasConsultation(boolean consultation) { this.hasConsultation = consultation; }
    public boolean hasConsultation()                     { return hasConsultation; }

    public void setNotification(String notification) { this.notification = notification; }
    public String getNotification()                  { return notification; }

    public List<Appointment> getAppointments() { return appointments; }

    public void bookAppointment(Department dept, TimeSlot slot) {
        if (!hasConsultation) throw new IllegalStateException("No Doctor's referral found. Cannot book this time.");
        appointments.add(new Appointment(patientID, dept, slot.getTime(), AppointmentStatus.CONFIRMED));
        slot.setAvailable(false);
    }

    @Override
    public String toString() { return name + ": [" + patientID + "]"; }
}
