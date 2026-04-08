package User;

import javafx.stage.Stage;

public abstract class Staff extends User { //Should be extended/implemented inside Doctor and Front desk class 
	
    protected String password;

    public Staff(String id, String name, String password) { //Might add some Enc <-> dec logic
        super(id, name);
        this.password = password;
    }

    public abstract void viewDashboard(Stage stage);

    @Override
    public String getID() { 
    	return id; 
    }

    @Override
    public String getName() { 
    	return name; 
    }

    public String getPassword() { 
    	return password; 
    }

    @Override
    public String toString() { 
    	return name + ": [" + id + "]"; 
    }

}