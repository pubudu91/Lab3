package cs4532.lab3;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class Passenger extends Thread implements Observer {
    private int passengerId;
    private BusStop busStop;
    private Bus currentBus;

    public Passenger(int id) {
        super("" + id);
        this.passengerId = id;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BusStop) {
            busStop = (BusStop) o;
            currentBus = busStop.getCurrentBus();

            synchronized (busStop) {
                if (currentBus != null)
                    busStop.notifyAll();
            }
        }
    }

    public void run() {
        randomSleep(); // Just to randomize things

        // This part checks whether the current bus is null. If so, it waits the thread.
        // The loop should eventually be moved to enclose the whole code inside the run() method.
        synchronized (busStop) {
            while (currentBus == null) {
                try {
                    busStop.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        boolean boarded;
        synchronized (busStop) {
            while ((boarded = currentBus.boardBus(this)) == false) {
                try {
                    busStop.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (boarded) {
            System.out.println("Passenger #" + passengerId + " boarded bus #" + currentBus.getBusId());
            return;
        } //else
//            System.out.println("Passenger #" + passengerId + " could not board bus #" + currentBus.getBusId());
    }

    private void randomSleep() {
        Random rnd = new Random(125);

        try {
            long n = rnd.nextLong() % 2000;
            Thread.sleep(n > 0 ? n : -n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
