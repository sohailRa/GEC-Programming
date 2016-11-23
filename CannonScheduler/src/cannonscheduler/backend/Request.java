package cannonscheduler.backend;

import java.util.Comparator;

public class Request implements Comparable {

    final private String groupName;
    final private int numberOfPeople;
    final private int seniority;
    final private int timeOfArrival;
    final private float duration;
    final private int actualDuration;    
    private float cost;

    public Request (
            String groupName,
            int numberOfPeople,
            int seniority,
            int timeOfArrival,
            float duration) {
        
        this.groupName = groupName;
        this.numberOfPeople = numberOfPeople;
        this.seniority = seniority;
        this.timeOfArrival = timeOfArrival - 8;
        this.duration = duration;
        this.actualDuration = (int) Math.ceil(duration);
        
        compute_cost();
    }
   
    private void compute_cost() {
        cost = (float) numberOfPeople + (float) seniority * 2 
                + duration * 6 - ((float) actualDuration - duration) * 6;
    }
    
    @Override
    public String toString() {
        return String.format("%2s %5d %5d %5d %10f %10d %15f", 
                groupName, numberOfPeople, seniority, 
                timeOfArrival+8, duration, actualDuration, 
                cost);
    }
    
    public float getCost() {
        return cost;
    }
    
     public int getTimeOfArrival() {
        return timeOfArrival;
    }
     
    public int getActualDuration() {
        return actualDuration;
    }
    
     public String getGroupName() {
        return groupName;
    }

    @Override
    public int compareTo(Object object) {
        
        Request request = (Request) object;
        if (this.cost > request.getCost()) {
            return -1;
        }
        else if (this.cost < request.getCost()) {
            return 1;
        }
                
        return 0;
    }
    
    static class TimeOfArrivalComparator implements Comparator<Request>
    {            
        @Override
        public int compare(Request request_1, Request request_2)
        {
            Integer time_1 = request_1.getTimeOfArrival();
            Integer time_2 = request_2.getTimeOfArrival();
            return time_1.compareTo(time_2);
        }
     }
    
    public static void main(String[] args) {
        System.out.println(new Request("a", 1, 2, 4, 2.3f));
    }
}
