package User;

import javafx.stage.Stage;

public abstract class Staff { //Should be extended/implemented inside Doctor and Front desk class 
	
    protected String staffID;
    protected String name;
    protected String password;

    public Staff(String staffID, String name, String password) { //Might add some Enc <-> dec logic
        this.staffID = staffID;
        this.name = name;
        this.password = password;
    }

    public abstract void viewDashboard(Stage stage);

    public String getStaffID() { 
    	return staffID; 
    }

    public String getName() { 
    	return name; 
    }

    public String getPassword() { 
    	return password; 
    }

    @Override
    public String toString() { 
    	return name + ": [" + staffID + "]"; 
    }

}