package cs4532.lab3;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by pubudu on 9/24/16.
 */
public class Passenger extends Thread implements Observer {
    private int passengerId;
    private Bus currentBus;

    public Passenger(int id) {
        super("" + id);
        this.passengerId = id;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BusStop) {
            currentBus = ((BusStop) o).getCurrentBus();

        }
    }

    public void run() {
//        System.out.println("Inside thread: "+ getName());
//        while (true) {
//            while (currentBus == null) ;
        
        boolean boarded = currentBus.boardBus(this);
        if (boarded) {
            System.out.println("Passenger #" + passengerId + " boarded bus #" + currentBus.getBusId());
            return;
        } else
            System.out.println("Passenger #" + passengerId + " could not board bus #" + currentBus.getBusId());
//        }
    }
}
