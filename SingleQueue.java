import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SingleQueue {

    private int SIMULATION_MINUTES = 480;
    private double ARRIVAL_PROBABILITY = 0.35;
    private int MIN_SERVICE_TIME = 3;
    private int MAX_SERVICE_TIME = 10;
    private int NUM_SERVERS = 3;
    private boolean VERBOSE = true;
    private boolean customerAdded = false;
    private Customer[] arrayOfQueues;
    private Queue<Customer> customerQueueList = new ArrayDeque<Customer>(); 


    private int customersServed = 0;
    private int currentID = 0;
    private double AvgWaitTime, MaxWaitTime, MaxQueueLength, ServerUtilization;
    private int totalWaitTime = 0;
        public SingleQueue() { 
            // Create the array container
            arrayOfQueues = new Customer[NUM_SERVERS];
            MaxWaitTime = 0;
            AvgWaitTime = 0;
            totalWaitTime = 0;
            MaxQueueLength = 0;
            ServerUtilization = 0;

        }

        public void run() {
            for (int tick = 0; tick < SIMULATION_MINUTES; tick++) {
                processDepartures(tick);
                processArrivals(tick);

                // Print tick-by-tick snapshot only when VERBOSE is enabled
                if (VERBOSE && tick < 30) { // Limit output to first 30 ticks during debug
                    printState(tick);
                }
            }
            
            // Always print final summary statistics regardless of VERBOSE flag
            //printSummaryStats();
        }

        public int customersRemaining(){
            int sum = 0;
            for (int i =0; i < NUM_SERVERS; i++){
                if (arrayOfQueues[i] != null){
                    sum ++;
                }
            }
            sum += customerQueueList.size();
            return sum;
        }

        public String toString(){
            String output = "====== SHARED QUEUE RESULTS ====== \n";
            output += "Total Customers Served: " + customersServed + " \n";
            output += "Average Wait Time: " + AvgWaitTime + " minutes \n";
            output += "Max Wait Time: " + MaxWaitTime + " minutes \n";
            output += "Max Queue Length: " + MaxQueueLength + " \n";
            output += "Server Utilization: " + Math.round((100 *ServerUtilization / (SIMULATION_MINUTES * NUM_SERVERS)) * 10.0) / 10.0  + "% \n";

            output += "Customers Remaining: " + customersRemaining() + " \n";

            return output;
        }


        public void processArrival(int tick, double random, Customer newArrival){
            if (random > ARRIVAL_PROBABILITY) {
                customerAdded = false;
                return;
            }
            currentID ++;
            customerAdded = true;       

            for (int i =0; i < arrayOfQueues.length; i++){
                if (arrayOfQueues[i] == null){
                    arrayOfQueues[i] = newArrival;
                    return ;
                }
            }

            int customersRemaining = customersRemaining();
            customerQueueList.add(newArrival);
            if (customersRemaining > MaxQueueLength){
                MaxQueueLength = customersRemaining;

            }

        }



        public void processArrivals(int tick){
            if (Math.random() > ARRIVAL_PROBABILITY) {
                customerAdded = false;
                return;
            }
            int waitTime = (int) (Math.random() * (MAX_SERVICE_TIME - (MIN_SERVICE_TIME - 1)))  + MIN_SERVICE_TIME;
            Customer newArrival = new Customer(waitTime, tick, currentID);
            currentID ++;
            customerAdded = true;       

            for (int i =0; i < arrayOfQueues.length; i++){
                if (arrayOfQueues[i] == null){
                    arrayOfQueues[i] = newArrival;
                    return ;
                }
            }

            //int customersRemaining = customersRemaining();
            customerQueueList.add(newArrival);
            if (customerQueueList.size() > MaxQueueLength){
                MaxQueueLength = customerQueueList.size();

            }

        }

        public void processDepartures(int tick){
            //System.out.println("PROCESSING TICK " + tick);
            for (int i =0; i < arrayOfQueues.length; i++){
                Customer serving = arrayOfQueues[i];
                if (serving == null) {
                    continue;
                }
                ServerUtilization ++;

                if (serving.getTimeLeft(tick) <= 0){
                    if (tick <= 30){
                    System.out.println("removing QUEUE: "  + i );
                    }
                    customersServed++;
                    int waitTime = arrayOfQueues[i].getTotalWait();
                    totalWaitTime += waitTime; 

                    AvgWaitTime = Math.round(10 * ((double)totalWaitTime /customersServed)) / 10.0;
                    
                    if (waitTime > MaxWaitTime){
                        MaxWaitTime = waitTime;
                    }
                    


                    arrayOfQueues[i] = null;

                    if (customerQueueList.isEmpty()){
                        continue;
                    }
                    
                    
                    arrayOfQueues[i] = customerQueueList.remove();
                    arrayOfQueues[i].setServeTime(tick);


                }
            }

        }

        public void printState(int tick) {
            System.out.println("=== time: " + tick + " ===");
            if (customerAdded == true){
                System.out.println("Customer #" + (currentID - 1) + " ARRIVED" );

            }

            System.out.println("Main Line Size: " + customerQueueList.size());

            for (int i =0; i < NUM_SERVERS; i++){
                
                Customer serving = arrayOfQueues[i];
                if (serving == null){
                    String output = "Queue " + i + ": EMPTY"; 
                    System.out.println(output);
                    continue;
                }
                String output = "Queue " + i + ": (Serving: " + serving.getID() + " Serve Time: " + serving.getTimeLeft(tick) + ")"; 
                System.out.println(output);

            }
            System.out.println("=========================================================");


        }


}