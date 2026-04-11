package function;

import java.util.PriorityQueue;
import java.util.Iterator;

public class PatientQueue {

    private PriorityQueue<PatientEnqueued> queue;

    public PatientQueue() {
        queue = new PriorityQueue<>(new PriorityComparator());
    }

    public void enqueue(PatientEnqueued patient) {
        queue.add(patient);
    }

    public PatientEnqueued dequeue() {
        return queue.poll();
    }

    public PatientEnqueued peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public Iterator<PatientEnqueued> getIterator() {
        return queue.iterator();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
