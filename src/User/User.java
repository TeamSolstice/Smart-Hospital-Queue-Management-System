package User;

public abstract class User {
	
	protected final String id;
	protected final String name;
	
	public User (String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public abstract String getName();
	public abstract String getID();
	
	@Override
    public String toString() {
    	return name + ": [" + id + "]"; 
    }
}
