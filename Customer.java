public class Customer{

    private int waitTime, arrivalTime, id;
    private int stallTime;
    public Customer(int wt, int at, int i){
        waitTime = wt;
        
        arrivalTime = at;
        stallTime = at;
        id = i;
    }

    public int getTotalWait(){
        return (stallTime - arrivalTime) + waitTime ;
    }

    public int getWaitTime(){
        return waitTime;
    }

    public void setServeTime(int input){
        stallTime = input;
    }


    public int getID(){
        return id;
    }

    public int getTimeLeft(int tick){
        return waitTime + stallTime - tick;
    }
}