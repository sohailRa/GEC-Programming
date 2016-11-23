package cannonscheduler.backend;

import java.util.ArrayList;

public class TimeSlot {
    
    private int startTime;
    private int actualDuration;
    private int totalDuration;
    private float cost;
    private String name;
    
    private TimeSlot prevTimeSlot;
    ArrayList<String> groupsUsed;
    
    
    public TimeSlot() {
        this.startTime = 0;
        this.actualDuration = 0;
        this.totalDuration = 0;
        this.cost = 0;
        name = "root";

        this.prevTimeSlot = null;
        
        groupsUsed = new ArrayList();
    }  
        
    public TimeSlot(TimeSlot newTimeSlot, TimeSlot prevTimeSlot, int startTime) {
        this.startTime = startTime;
        this.actualDuration = newTimeSlot.getActualDuration();
        
        this.totalDuration = newTimeSlot.getActualDuration() + prevTimeSlot.getTotalDuration();
        this.cost = newTimeSlot.getCost() + prevTimeSlot.getCost();
        this.name = newTimeSlot.getName();
        
        this.prevTimeSlot = prevTimeSlot;
        
        groupsUsed = (ArrayList<String>) prevTimeSlot.getGroupsUsed().clone();
        groupsUsed.add(newTimeSlot.getName());
    } 
        
    public TimeSlot(Request request) {
        this.startTime = request.getTimeOfArrival();
        this.actualDuration = request.getActualDuration();
        this.totalDuration = actualDuration;
        this.cost = request.getCost();
        this.name = request.getGroupName();
        this.prevTimeSlot = null;
    }
    
    public TimeSlot(Request request, TimeSlot previous, int startTime) {
        this.startTime = Math.max(startTime, previous.getStartTime()+previous.getActualDuration());
        this.actualDuration = request.getActualDuration();
        this.totalDuration = previous.getTotalDuration() + actualDuration;
        this.cost = previous.getCost() + request.getCost();
        this.name = request.getGroupName();
        this.prevTimeSlot = null;
    }
    
    public void addUsedGroup(String name) {
        groupsUsed.add(name);
    }
    
    @Override
    public String toString() {
        return String.format("%4s %5d %5d %5d %15f", 
                name, startTime + 8, actualDuration, totalDuration, cost);
    }
    
    
    public int getStartTime() {
        return startTime;
    }
    
    public int getActualDuration() {
        return actualDuration;
    }
     
    public int getTotalDuration() {
        return totalDuration;
    }
    
    public float getCost() {
        return cost;
    }
    
    public String getName() {
        return name;
    }
    
    public TimeSlot getPrevTimeSlot() {
        return prevTimeSlot;
    }
    
    public ArrayList<String>  getGroupsUsed() {
        return groupsUsed;
    }
    
    public boolean isGroupUsed(String name) {
        return groupsUsed.contains(name);
    }
      
}
