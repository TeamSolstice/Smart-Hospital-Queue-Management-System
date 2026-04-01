package function;

public class TimeSlot {
	
    private String time;
    private boolean available;

    public TimeSlot(String time) {
        this.time = time;
        this.available = true;
    }

    public String getTime() { 
    	return time; 
    }
    
    public boolean isAvailable() { 
    	return available; 
    }
    
    public void setAvailable(boolean b) { 
    	this.available = b; 
    }

    @Override
    public String toString() { return time + (available ? "" : " (Booked)"); }
}