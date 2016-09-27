/**
 * Created by yellowflash on 9/23/16.
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;


public class main {

    public static void main(String[] args) {
        int numberOfPassegers = 50;
        int numberOfBusesTotal = 10;
        int numberOfRidersTotal = 500;
        int delayBus = 10000;
        int delayBusMean = 30; // mean in seconds
        int delayRiderMean = 3; // mean in seconds
        int delayRiders = 3000;

        Timer bus_timer = new Timer();
        Timer rider_timer = new Timer();

        Semaphore multipleLock = new Semaphore(1);
        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore boardPassengers = new Semaphore(0);

        class Rider_task extends TimerTask {

            @Override
            public void run() {
                Thread t = null;
                t = new Thread(new Rider(multipleLock, mutex, bus, boardPassengers), "Rider_" + scheduledExecutionTime());
                System.out.println("Rider Arrived");
                t.start();
            }
        }

        class Bus_task extends TimerTask {

            @Override
            public void run() {
                Thread t = null;
                t = new Thread(new Bus(mutex, bus, boardPassengers), "Bus");
                System.out.println("Bus Arrived");
                t.start();
            }
        }

        for (int i = 0; i < numberOfBusesTotal; i++) {
            bus_timer.schedule(new Bus_task(), delayBus);
            delayBus += Math.abs(getRandomInterTimeInterval(delayBusMean * 1000));
        }

        for (int i = 0; i < numberOfRidersTotal; i++) {
            rider_timer.schedule(new Rider_task(), delayRiders);
            delayRiders += Math.abs(getRandomInterTimeInterval(delayRiderMean * 1000));
        }


    }

    public static int getRandomInterTimeInterval(double meanTimeInterval) {
        double random = Math.random();
        double val = (-1) * (meanTimeInterval) * (Math.log(random * meanTimeInterval));
        return (int) val;
    }
}


class Rider implements Runnable {
    Semaphore multipleLock, mutex, bus, boardPassengers;

    public Rider(Semaphore multipleLock, Semaphore mutex, Semaphore bus, Semaphore boardPassengers) {
        this.multipleLock = multipleLock;
        this.mutex = mutex;
        this.bus = bus;
        this.boardPassengers = boardPassengers;
    }


    @Override
    public void run() {

//        try {
//            mutex.acquire();
//        } catch (InterruptedException e) {
//            mutex.release();
//        }
        if (mutex.availablePermits() < 1) {
            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                mutex.release();
            }
        }


        try {
            multipleLock.acquire();
        } catch (InterruptedException e) {
            multipleLock.release();
        }
        BusStop.riders++;
        multipleLock.release();


        try {
            bus.acquire();
        } catch (InterruptedException e) {
            bus.release();
        }

        System.out.println("boarding");

        BusStop.riders--;
        BusStop.boardedCount++;
        if (BusStop.riders == 0 || BusStop.boardedCount == 50) {
            boardPassengers.release();
        } else {
            bus.release();
        }
    }
}

class Bus implements Runnable {

    Semaphore mutex, bus, boardPassengers;

    public Bus(Semaphore mutex, Semaphore bus, Semaphore boardPassengers) {

        this.mutex = mutex;
        this.bus = bus;
        this.boardPassengers = boardPassengers;

    }

    @Override
    public void run() {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            mutex.release();
        }
        if (BusStop.riders > 0) {

            bus.release();
            try {
                boardPassengers.acquire();
            } catch (InterruptedException e) {
                boardPassengers.release();
            }
        }

        mutex.release();
        BusStop.boardedCount = 0;
        System.out.println("Depart");
    }
}

class BusStop {
    public static int riders = 0;
    public static int boardedCount = 0;
}