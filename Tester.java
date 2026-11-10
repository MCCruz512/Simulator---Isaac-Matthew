/**
 * Tester to test methods
 * 
 * @author mc
 * @version 9926
 */

public class Tester{
    public static void main(String[] args){
        int MIN_SERVICE_TIME = 3;
        int MAX_SERVICE_TIME = 10;
        int currentID = 0;

        MultiQueue multi = new MultiQueue();
        SingleQueue single = new SingleQueue();



        for (int tick = 0; tick < 480; tick++) {

            multi.processDepartures(tick);
            single.processDepartures(tick);

            double random = Math.random();

            if (random < .35){
                int waitTime = (int) (Math.random() * (MAX_SERVICE_TIME - (MIN_SERVICE_TIME - 1)))  + MIN_SERVICE_TIME;
                currentID ++;
                multi.processArrival(tick, random, new Customer(waitTime, tick, currentID));
                single.processArrival(tick, random, new Customer(waitTime, tick, currentID));
            } else{
                multi.processArrival(tick, random, null);
                single.processArrival(tick, random, null);
            }

            System.out.println("MULTI STATE: ");
            multi.printState(tick);
            if (tick <= 30){

                //System.out.println("SINGLE STATE: ");
                //single.printState(tick);
            }

            
            // processDepartures(tick);
            // processArrivals(tick);

            // Print tick-by-tick snapshot only when VERBOSE is enabled
            // if (VERBOSE && tick < 30) { // Limit output to first 30 ticks during debug
            //     printState(tick);
            // }
        }

        //multi.run();
        System.out.println("SINGLE QUEUE ====================================");
        //single.run();

        System.out.println("==== QUEUE RESULTS ===== \n ");
        System.out.println(multi);

        System.out.println(single);

    }
}
