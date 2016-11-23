package cannonscheduler.backend;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Scheduler {
    
    ArrayList<Request> requests;
    
    public Scheduler() {
        requests = new ArrayList();
    }
    
    public void addRequest(Request request) {
        requests.add(request);
    }
    
    public void addRequests(ArrayList<Request>  requests) {
        this.requests.addAll(requests);
    }
    
    public void print() {
        for (Request request : requests) 
            System.out.println(request);
    }
    
    public void sortByCost() {
        Collections.sort(requests);
    }
    
    abstract public ArrayList<TimeSlot> schedule();
}
