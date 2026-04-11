package function;

import java.util.Iterator;
import java.util.PriorityQueue;

public class PatientQueue {
	
	PriorityQueue<PatientEnqueued> queue;
	
	public PatientQueue() {
		this.queue = new PriorityQueue<PatientEnqueued>(new PriorityComparator());
	}
	
	public void add(PatientEnqueued patient) {
		queue.add(patient);
	}
	
	public PatientEnqueued dequeue() {
		return queue.poll();
	}
	
	public Iterator<PatientEnqueued> getIterator() {
		return queue.iterator();
	}
}
