package cannonscheduler.test;

import cannonscheduler.backend.BruteForceScheduler;
import cannonscheduler.backend.HeuristicScheduler;
import cannonscheduler.backend.Parser;
import cannonscheduler.backend.Request;
import cannonscheduler.backend.TimeSlot;
import static com.sun.xml.internal.ws.api.message.Packet.Status.Request;
import java.util.ArrayList;

public class SchedulerTest {
    
     public static void main(String[] args) {
        
        
        float averageCostError = 0;
        
        int numberOfIterations = 100;
        for (int testIterations = 0; testIterations < numberOfIterations; testIterations++) {

            BruteForceScheduler scheduler = new BruteForceScheduler();
            HeuristicScheduler hScheduler = new HeuristicScheduler();
            
            int numOfRandInputs =   8;
            char name = 'A';

            for (int i = 0; i < numOfRandInputs; i++) {
                int number_of_people = 1 + (int)(Math.random() * 6); 
                int seniority = 1 + (int)(Math.random() * 5); 
                int time_of_arrival = 8 + (int)(Math.random() * 8); 
                int hour = (int)(Math.random() * 2); 
                int minute = (int)(Math.random() * 59); 

                scheduler.addRequest(new Request(Character.toString(name) + "r", 
                        number_of_people, seniority, time_of_arrival,  (float) hour + ((float) minute / 60)));

                hScheduler.addRequest(new Request(Character.toString(name) + "r", 
                        number_of_people, seniority, time_of_arrival,  (float) hour + ((float) minute / 60)));

                name += 1;
            }

            Parser parser = new Parser("testData.csv"); //test Parser
    //        scheduler.addRequests(parser.getRequests());
    //        markScheduler.addRequests(parser.getRequests());

            System.out.println("Number of Inputs: " + numOfRandInputs);
            
            long startTime = System.currentTimeMillis();
            ArrayList<TimeSlot> scheduled = scheduler.schedule();
            long endTime = System.currentTimeMillis();
            long searchTime = endTime - startTime;
            System.out.println("BruteForce CPU Time Usage: " + searchTime + " ms");
            
            startTime = System.currentTimeMillis();    
            ArrayList<TimeSlot> hScheduled = hScheduler.schedule();
            endTime = System.currentTimeMillis();
            searchTime = endTime - startTime;
            System.out.println("Heuristic CPU Time Usage: " + searchTime + " ms");


//            System.out.println("Output:");
//            for (int i = 0; i < scheduled.size(); i++) {
//                System.out.println(scheduled.get(i));
//            }   
//            System.out.println();
//            for (int i = 0; i < hScheduled.size(); i++) {
//                System.out.println(hScheduled.get(i));
//            } 
//            System.out.println();
            
            if (!scheduled.isEmpty() && !hScheduled.isEmpty()) { 
                float costError = scheduled.get(scheduled.size()-1).getCost() 
                        - hScheduled.get(hScheduled.size()-1).getCost();
                averageCostError += costError;
                System.out.print("costError: " + costError);
            }
            System.out.println("\n");
        }
        System.out.println("averageCostError " + averageCostError/numberOfIterations);
     }
}
