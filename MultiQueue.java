import java.util.ArrayDeque;
import java.util.Queue;

public class MultiQueue {

    private int SIMULATION_MINUTES = 480;
    private double ARRIVAL_PROBABILITY = 0.35;
    private int MIN_SERVICE_TIME = 3;
    private int MAX_SERVICE_TIME = 10;
    private int NUM_SERVERS = 3;
    private boolean VERBOSE = true;
    private boolean customerAdded = false;
    private Customer lead;
    private Queue<Customer>[] arrayOfQueues;
    private int customersServed = 0;
    private int currentID = 0;
    private double AvgWaitTime, MaxWaitTime, MaxQueueLength, ServerUtilization;
    private int totalWaitTime;
    @SuppressWarnings("unchecked")
        public MultiQueue() { 
            // Create the array container
            arrayOfQueues = (Queue<Customer>[]) new Queue[NUM_SERVERS]; 
            
            // 2. Initialize each slot with an array-backed queue
            for (int i = 0; i < NUM_SERVERS; i++) {
                arrayOfQueues[i] = new ArrayDeque<Customer>(); 
            }

            MaxWaitTime = 0;
            AvgWaitTime = 0;
            totalWaitTime = 0;
            MaxQueueLength = 0;
            ServerUtilization = 0; //NUM_SERVERS * SIMULATION_MINUTES;

        }

        public void run() {
            for (int tick = 0; tick < SIMULATION_MINUTES; tick++) {
                processDepartures(tick);
                processArrivals(tick);
                dispatchCustomers(tick);

                // Print tick-by-tick snapshot only when VERBOSE is enabled
                if (VERBOSE && tick < 30) { // Limit output to first 30 ticks during debug
                    printState(tick);
                }
            }
            
            // Always print final summary statistics regardless of VERBOSE flag
        }

        private int customersRemaining(){
            int sum = 0;
            for (int i =0; i < arrayOfQueues.length; i++){
                Queue<Customer> queue = arrayOfQueues[i];
                sum += queue.size();
            }
            return sum;
        }





        public String toString(){
            String output = "====== MULTI QUEUE RESULTS ====== \n";
            output += "Total Customers Served: " + customersServed + " \n";
            output += "Average Wait Time: " + AvgWaitTime + " minutes \n";
            output += "Max Wait Time: " + MaxWaitTime + " minutes \n";
            output += "Max Queue Length: " + MaxQueueLength + " \n";
            output += "Server Utilization: " + Math.round((100 *ServerUtilization / (SIMULATION_MINUTES * NUM_SERVERS)) * 10.0) / 10.0  + "% \n";

            output += "Customers Remaining: " + customersRemaining() + " \n";

            return output;
        }
        private void dispatchCustomers(int tick){
            
        }

        public void processArrival(int tick, double random, Customer newArrival){
            if (random > ARRIVAL_PROBABILITY) {
                customerAdded = false;
                return;
            }
            Queue<Customer> smallest = arrayOfQueues[0];
            int smallestLength = arrayOfQueues[0].size(); 


            int totalSize = 0;

            for (int i =0; i < arrayOfQueues.length; i++){
                Queue<Customer> queue = arrayOfQueues[i];

                if (queue.size() <= smallestLength){
                    smallest = queue;
                    smallestLength = queue.size();
                }
                totalSize += Math.max(queue.size() - 1, 0);
            }


            if (totalSize > MaxQueueLength){
                MaxQueueLength = totalSize;
            }
            currentID ++;
            smallest.add(newArrival);
            customerAdded = true;
        }

        public void processArrivals(int tick){
            if (Math.random() > ARRIVAL_PROBABILITY) {
                customerAdded = false;
                return;
            }
            Queue<Customer> smallest = arrayOfQueues[0];
            int smallestLength = arrayOfQueues[0].size(); 

            for (int i =0; i < arrayOfQueues.length; i++){
                Queue<Customer> queue = arrayOfQueues[i];

                if (queue.size() <= smallestLength){
                    smallest = queue;
                    smallestLength = queue.size();
                }
            }

            int totalSize = customersRemaining();

            if (totalSize > MaxQueueLength){
                MaxQueueLength = totalSize;
            }

            int waitTime = (int) (Math.random() * (MAX_SERVICE_TIME - (MIN_SERVICE_TIME - 1)))  + MIN_SERVICE_TIME;
            Customer newArrival = new Customer(waitTime, tick, currentID);
            currentID ++;
            smallest.add(newArrival);
            customerAdded = true;
    }

        public void processDepartures(int tick){
            //System.out.println("PROCESSING TICK " + tick);
            for (int i =0; i < arrayOfQueues.length; i++){
                Queue<Customer> queue = arrayOfQueues[i];
                if (queue.isEmpty()){
                    continue;
                }
                ServerUtilization ++; 
                lead = queue.peek();
                //System.out.println("timeleft: " + lead.getTimeLeft(tick) +    " QUEUE " + i);
                if (lead.getTimeLeft(tick) <= 0){
                    if (tick <= 30){
                    //System.out.println("removing QUEUE: "  + i );

                    }
                    Customer removed = queue.remove();
                    int waitTime = removed.getTotalWait();
                    //System.out.println(waitTime);
                    customersServed++;

                    totalWaitTime += waitTime; 
                    AvgWaitTime = Math.round(10 * ((double)totalWaitTime /customersServed)) / 10.0;
                    //System.out.println(AvgWaitTime)
                    if (waitTime > MaxWaitTime){
                        MaxWaitTime = waitTime;
                    }
                    System.out.println(AvgWaitTime + "  " + totalWaitTime + " " + customersServed);

                    if (queue.peek() != null){
                        queue.peek().setServeTime(tick);

                    }



                }
            }

        }

        public void printState(int tick) {
            System.out.println("=== time: " + tick + " ===");
            if (customerAdded == true){
                System.out.println("Customer #" + (currentID - 1) + " ARRIVED" );

            }

            for (int i =0; i < NUM_SERVERS; i++){
                Queue<Customer> queue = arrayOfQueues[i];
                if (queue.isEmpty()){
                    String output = "Queue " + i + ": EMPTY"; 
                    System.out.println(output);
                    continue;
                }
                Customer front = queue.peek();
                String output = "Queue " + i + ": (Serving: " + front.getID() + " ServeTime: " + front.getTimeLeft(tick) + ") size: " + queue.size(); 
                System.out.println(output);

            }
            System.out.println("=========================================================");


        }


}