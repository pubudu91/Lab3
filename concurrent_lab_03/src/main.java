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
        int delayBusMean=20 * 60; // mean in seconds
        int delayRiderMean=3; // mean in seconds
        int delayRiders = 3000;

        Timer bus_timer = new Timer();
        Timer rider_timer = new Timer();

        Semaphore multipleLock = new Semaphore(numberOfPassegers);
        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore allAboard = new Semaphore(0);

        class Rider_task extends TimerTask {

            @Override
            public void run() {
                Thread t = null;
                t = new Thread(new Rider(multipleLock, mutex, bus, allAboard), "Rider_" + scheduledExecutionTime());
                System.out.println("Rider Arrived");
                t.start();
            }
        }

        class Bus_task extends TimerTask {

            @Override
            public void run() {
                Thread t = null;
                t = new Thread(new Bus(mutex, bus, allAboard), "Bus");
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
    Semaphore multipleLock, mutex, bus, allAboard;

    public Rider(Semaphore multipleLock, Semaphore mutex, Semaphore bus, Semaphore allAboard) {
        this.multipleLock = multipleLock;
        this.mutex = mutex;
        this.bus = bus;
        this.allAboard = allAboard;
    }


    @Override
    public void run() {

        try {
            multipleLock.acquire();
        } catch (InterruptedException e) {
            multipleLock.release();
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            mutex.release();
        }

        BusStop.riders++;
        mutex.release();

        try {
            bus.acquire();
        } catch (InterruptedException e) {
            bus.release();
        }

        System.out.println("boarding");

        BusStop.riders--;
        if (BusStop.riders == 0) {
            allAboard.release();
        } else {
            bus.release();
        }
    }
}

class Bus implements Runnable {

    Semaphore mutex, bus, allAboard;

    public Bus(Semaphore mutex, Semaphore bus, Semaphore allAboard) {

        this.mutex = mutex;
        this.bus = bus;
        this.allAboard = allAboard;

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
                allAboard.acquire();
            } catch (InterruptedException e) {
                allAboard.release();
            }
        }

        mutex.release();
        System.out.println("Depart");
    }
}

class BusStop {
    public static int riders = 0;
}