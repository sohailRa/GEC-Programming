package cannonscheduler.backend;

import java.util.ArrayList;

public class Bench {
    
    public float cost;
    public boolean[][] tracker;
    public String[] requestOrder;
    
    public Bench(int benchSize) {
        tracker = new boolean[3][benchSize];
        cost = 0;
        requestOrder = new String[14];
    }
}
