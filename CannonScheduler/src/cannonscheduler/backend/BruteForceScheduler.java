package cannonscheduler.backend;

import java.util.ArrayList;
import java.util.Collections;

public class BruteForceScheduler extends Scheduler {
    
    ArrayList<TimeSlot> timeSlots;
    
    float max_cost;
    TimeSlot bestTimeSlot;
    
    public BruteForceScheduler() {
        super();
        
        timeSlots = new ArrayList();
        
        max_cost = 0;
        bestTimeSlot = null;
    }
    
    @Override
    public ArrayList<TimeSlot> schedule() {
        for (Request request : requests) 
            timeSlots.add(new TimeSlot(request));
        
        TimeSlot root = new TimeSlot();
        scheduleNext(root, 0);
        
        ArrayList<TimeSlot> scheduled = new ArrayList();
        reformatBest(bestTimeSlot, scheduled);
        
        return scheduled;
    }
    
    
    public void scheduleNext(TimeSlot currentBranch, int level) {
        
        if (level > 11) return;
        if (currentBranch.getTotalDuration() > 11) return;
        
        for (TimeSlot possibleTimeSlot : timeSlots) {
            if (!currentBranch.isGroupUsed(possibleTimeSlot.getName())) {
                if (!currentBranch.getName().equals(possibleTimeSlot.getName())) {
                    if (level >= possibleTimeSlot.getStartTime()) { // Check if timeSlot can be started at this time
                        TimeSlot next;
                        next = new TimeSlot(possibleTimeSlot, currentBranch, 
                                Math.max(level, currentBranch.getActualDuration()+currentBranch.getStartTime()));

                        if (next.getTotalDuration() > 11) return;
                        if (next.getStartTime() + next.getActualDuration() > 19-8) return;

                        if (max_cost < next.getCost()) {
                            max_cost = next.getCost();
                            bestTimeSlot = next;
                        }

                        scheduleNext(next, level + 1);
                    }
                    else {
                        scheduleNext(currentBranch, level + 1);
                    }
                }
            }
        }  
        
    }
    
    public void reformatBest(TimeSlot timeSlot, ArrayList<TimeSlot> scheduled) {
        if(timeSlot == null) return;
        
        if (timeSlot.getPrevTimeSlot() != null) 
            reformatBest(timeSlot.getPrevTimeSlot(), scheduled); 
        if (timeSlot.getName().equals("root")) return;
        scheduled.add(timeSlot);
    }

    public static void main(String[] args) {
        
	Parser parser = new Parser("testData.csv"); 
        BruteForceScheduler scheduler = new BruteForceScheduler();
        scheduler.addRequests(parser.getRequests());
        
        long startTime = System.currentTimeMillis();
        ArrayList<TimeSlot> scheduled = scheduler.schedule();
        long endTime = System.currentTimeMillis();
        
        for (TimeSlot timeSlot : scheduled)
            System.out.println(timeSlot);
        
        
        long searchTime = endTime - startTime;
        System.out.println("BruteForce CPU Time Usage: " + searchTime + " ms");
        
    }
    
}
