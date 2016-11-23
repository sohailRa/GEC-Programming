package cannonscheduler.backend;

import java.util.ArrayList;
import java.util.Collections;

public class HeuristicScheduler extends Scheduler {
    
    private ArrayList<ArrayList<Request>> hourRequests;
    private int[][] hourIndexes;
    
    public HeuristicScheduler() {
        super();
    }
    
    private void prepareForScheduling() {
        hourRequests = new ArrayList();
        for (int i = 0; i < 3; i++) {
            hourRequests.add(new ArrayList());
        }
        
        hourIndexes = new int [3][11];
        
        Collections.sort(requests, new Request.TimeOfArrivalComparator());
        
        for (Request request: requests) {
            int actualDuration = request.getActualDuration() - 1;
            
            if (actualDuration < 0 || actualDuration > 2) continue;
            hourRequests.get(actualDuration).add(request);
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                for (Request request: hourRequests.get(i)) {
                    if (request.getTimeOfArrival() == j) {
                        hourIndexes[i][j]++;
                    }
                    if (j < 10) hourIndexes[i][j + 1] = hourIndexes[i][j];
                }
            }
        }
    }
    
    public void printArrayList(ArrayList<ArrayList<Request>> nested) {
        for (ArrayList<Request> list : nested) {
            for (Request request : list)
                System.out.println(request);
            System.out.println();
        }
    }
    
    public void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++)
                System.out.println((j + 8) + " " + array[i][j]);
        System.out.println();
    }
    
    @Override
    public ArrayList<TimeSlot> schedule() {
        prepareForScheduling();
        
//        printArray(hourIndexes);
//        printArrayList(hourRequests);
        
        ArrayList<Request> scheduledRequests = new ArrayList();
        
        float[] max = new float[3];
        int[] maxIndices = new int[3];
        Bench[] bench = new Bench[14];
        for (int i = 0; i < 14; i++) {
            bench[i] = new Bench(requests.size());
        }
        
        // Tracks when new values are being added
        boolean[] flag = new boolean[3];
        for (int i = 0; i < 9; i++) { // Iteration through time slots
            for (int j = 0; j < 3; j++) {
                
                //Clearing and reinitializing temporary variables
                flag[j] = false; 
                max[j] = -6; //Passive cost of an empty hour
                maxIndices[j] = 0;
                
                //Compute the maximum value for each hour time block
                for (int k = 0; k < hourIndexes[j][i]; k++) {
                    Request request = hourRequests.get(j).get(k);
                    //Only update the maximum if the value isn't being used
                    if (bench[i + 2 - j].tracker[j][k] == false) {
                        if (max[j] + 6 < request.getCost()) {
                            max[j] = request.getCost();
                            maxIndices[j] = k;
                            flag[j] = true; //
//                            System.out.format("%d: %f, \n", j, request.getCost());
                        }
                    }
                }
                //System.out.println();
            }
            
            float tempMax = 0; //Used to compute the highest cost
            int   tempIndex = 0; //Stores which previous value corresponds to max
            
            //Compute the maximum value of the cost, based on the 3 prior terms
            //If no available requests, max is zero, just gets best of prior 3
            for (int j = 0; j < 3; j++) {
                float temp = bench[i+j].cost + max[2 - j];
                if (tempMax < temp){
                    tempMax = temp;
                    tempIndex = j;
                }
            }
            
            for (int j = 0; j < 3; j++) {
                //Set the current tracker equal to the selected term in sequence
                for (int k = 0; k < bench[i+3].tracker[j].length; k++) {
                    bench[i+3].tracker[j][k] = bench[i + tempIndex].tracker[j][k];
                }
                //Set the order of requests to be equal to the previous term in sequence
                for (int k = 0; k < i; k++)
                    bench[i+3].requestOrder[k] = bench[i + tempIndex].requestOrder[k];
            }
            
            bench[i+3].cost = tempMax;
            //If a new request was added, update the tracker and the request list
            if(flag[2-tempIndex]) {
                bench[i+3].tracker[2-tempIndex][maxIndices[2-tempIndex]] = true;
                if (hourRequests.size() != 0) {
                    bench[i+3].requestOrder[i] = hourRequests.get(2-tempIndex).get(maxIndices[2-tempIndex]).getGroupName();
                    scheduledRequests.add(hourRequests.get(2-tempIndex).get(maxIndices[2-tempIndex]));
                }
            }
            
//            System.out.println("\n\nAnother one");
//            for (int j = 0; j < 3; j++) {
//                System.out.print(j + " -> ");
//                for (int k = 0; k < bench[i+tempIndex].tracker[j].length; k++) {
//                    if(bench[i+3].tracker[j][k])
//                        System.out.print(k + " ");
//                }
//                System.out.println();
//            }
//            for(int j = 0; j <= i; j++)
//                System.out.print(bench[i+3].requestOrder[j]);
//            System.out.println();
        }
//        for(int j = 0; j <= 11; j++)
//            System.out.print(bench[11].requestOrder[j] + " ");
//        System.out.println();
        
        // prepare scheduled timeslots
        ArrayList<TimeSlot> scheduled = new ArrayList();
        for (int i = 0; i <= 11; i++ ) {
            String groupName = bench[11].requestOrder[i];
            if (groupName != null) {
                for (Request request : requests) {
                    if (request.getGroupName().equals(groupName)) {
//                        System.out.println(request.getGroupName() + " " + groupName);
                        if (scheduled.isEmpty())
                            scheduled.add(new TimeSlot(request));
                        else
                            scheduled.add(new TimeSlot(request, 
                                    scheduled.get(scheduled.size()-1), i));
                    }
                }
            }
        }
        
        
        return scheduled;
    }
    
    

    public static void main(String[] args) {
        
        
        Parser parser = new Parser("testData.csv"); //test Parser
        HeuristicScheduler scheduler = new HeuristicScheduler();
        scheduler.addRequests(parser.getRequests());
        
        long startTime = System.currentTimeMillis();
        ArrayList<TimeSlot> scheduled = scheduler.schedule();
        long endTime = System.currentTimeMillis();
        
        for (int i = 0; i < scheduled.size(); i++) {
            System.out.println(scheduled.get(i));
        } 
        System.out.println();
        
        
        long searchTime = endTime - startTime;
        System.out.println("Heuristic CPU Time Usage: " + searchTime + " ms");
    }
    
}
