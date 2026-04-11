package function;

import java.util.Comparator;

public class PriorityComparator implements Comparator<PatientEnqueued> {

    @Override
    public int compare(PatientEnqueued a, PatientEnqueued b) {
        if (a.getPriority().equalsIgnoreCase("emergency") && 
            b.getPriority().equalsIgnoreCase("regular")) {
            return -1;
        } else if (a.getPriority().equalsIgnoreCase("regular") && 
                   b.getPriority().equalsIgnoreCase("emergency")) {
            return 1;
        } else {
            return 0;
        }
    }
}
