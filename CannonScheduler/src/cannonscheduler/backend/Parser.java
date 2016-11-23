package cannonscheduler.backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    
    ArrayList<Request> reqList;
    
    public Parser(String fileName) {
        
        reqList = new ArrayList<>();
        
        BufferedReader reader;
        try {
             reader = new BufferedReader(new FileReader(fileName));
        }
        catch (FileNotFoundException e) {
            return;
        }

        // read file line by line
        String line = null;
        Scanner scanner = null;
        int index = 0;
                
        String group_name = "";
        int number_of_people = 0;
        int seniority = 0;
        int time_of_arrival = 0;
        int dur_hour = 0;
        float dur_mint = 0;
        float duration = 0;

        try {
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                    scanner = new Scanner(line);
                    scanner.useDelimiter(",");
                    while (scanner.hasNext()) {
                            String data = scanner.next();
                            if (index == 0)
                                    group_name = data;
                            else if (index == 1) {
                                if (data.matches("[1-6]{1}"))
                                    number_of_people = Integer.parseInt(data);
                                else
                                    number_of_people = 1;
                            }
                            else if (index == 2) {
                                if (data.matches("[1-5]{1}"))
                                    seniority = Integer.parseInt(data);
                                else
                                    seniority = 1;
                            }
                            else if (index == 3) {
                                if (data.matches("(0?[8-9]{1})|(1[0-6]{1})"))
                                    time_of_arrival  = Integer.parseInt(data);
                                else
                                    time_of_arrival = 16;
                            }
                            else if (index == 4) {
                                if (data.matches("[0-3]{1}"))
                                    dur_hour  = Integer.parseInt(data);
                                else
                                    dur_hour = 1;
                            }
                            else if (index == 5) {
                                if (data.matches("(0?[0-9]{1})|([0-5]{1}[0-9]{1})"))
                                    dur_mint  = Float.parseFloat(data);
                                else
                                    dur_mint = 1;
                            }
                            else
                                    System.out.println("invalid data::" + data);

                            index++;        
                    }
                    index = 0;
                    duration = ((dur_mint/60) + dur_hour);
                    if (duration > 3.0f) duration = 3.0f;
                    Request req = new Request(group_name, number_of_people, seniority, time_of_arrival, duration);
                    reqList.add(req);
            }
            //close reader
            reader.close();
        }
        catch (IOException e) {
            return;
        }
    }
        
    public ArrayList<Request> getRequests() {
        return reqList;
    }
    
    public static void main(String[] args) {
	Parser parser = new Parser("testData.csv"); //test Parser
        
        
        for (Request request : parser.getRequests())
            System.out.println(request);	
    }
}



