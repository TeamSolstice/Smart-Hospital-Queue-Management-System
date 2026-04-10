package function;

public enum Priority {
	EMERGENCY(1), REGULAR(3);
	
	private final int priorityLevel;
	
	Priority(int priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	
	public int getPriorityLevel() {
		return priorityLevel;
	}
}
