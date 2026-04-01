package function;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private String location;
    private int waitTime;
    private List<TimeSlot> availableSlots;

    public Department(String name, String location, int waitTime, List<TimeSlot> slots) {
        this.name = name;
        this.location = location;
        this.waitTime= waitTime;
        if(slots != null) this.availableSlots = slots; 
        else this.availableSlots = new ArrayList<>(); //If slot is not exist, make new slots
    }

    public String getName() { 
    	return name; 
    }
    
    public String getLocation() { 
    	return location; 
    }
    
    public int getWaitTime() { 
    	return waitTime; 
    }
    
    public void setWaitTime(int min) { 
    	this.waitTime = min; 
    }
    
    public List<TimeSlot> getAvailableSlots() { 
    	return availableSlots; 
    }

    @Override
    public String toString() {
        return name + "  |  " + location + "  |  Estimated time for waiting: " + waitTime + " min";
    }
}