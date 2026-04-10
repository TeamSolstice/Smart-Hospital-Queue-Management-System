package function;

import java.util.Comparator;

public class PriorityComparator implements Comparator<PatientEnqueued> {
	
	// REMINDER:
	// EMERGENCY priority level code is 1
	// REGULAR priority level code is 3
	
	public int compare(PatientEnqueued patient1, PatientEnqueued patient2) {
		// if emergency over regular, return -1
		if (patient1.getPriorityLevelCode() < patient2.getPriorityLevelCode()) {
			return -1;
		} else if (patient1.getPriorityLevelCode() == patient2.getPriorityLevelCode()) {
			// if same priority, evaluate based on time queued
			
			if (patient1.getTimeQueued().getHour() < patient2.getTimeQueued().getHour()) {
				// if hour comes before patient 2's, return -1
				return -1;
			} else if (patient1.getTimeQueued().getHour() == patient2.getTimeQueued().getHour()) {
				
				// if same hour, evaluate based on minutes
				if (patient1.getTimeQueued().getMinute() < patient2.getTimeQueued().getMinute()) {
					// if minutes are less, return -1
					return -1;
				} else if (patient1.getTimeQueued().getMinute() == patient2.getTimeQueued().getMinute()) {
					// if minutes are same, return 0
					return 0;
				} else {
					// if minutes of patient 1 is higher than patient 2's, return 1
					return 1;
				}
			} else {
				// if patient1's hour is greater than patient2, return 1
				return 1;
			}
		} else {
			// if patient1's priority code number is higher than patient2's, return 1
			return 1;
		}
	}
}
