/**
 * Created by yellowflash on 9/23/16.
 */

import java.util.concurrent.Semaphore;


public class main {

    public static void main(String[] args) {
        int numberOfPassegers = 50;
        Semaphore multipleLock = new Semaphore(numberOfPassegers);
        Semaphore mutex = new Semaphore(1);
        Semaphore bus = new Semaphore(0);
        Semaphore allAboard = new Semaphore(0);

        Thread t = null;
        for (int i = 0; i < numberOfPassegers; i++) {
            t = new Thread(new Rider(multipleLock, mutex, bus, allAboard), "Rider_" + i);
            t.start();
            System.out.println("rider created");
        }


        t = new Thread(new Bus(mutex, bus, allAboard), "Bus");
        t.start();
        System.out.println("bus created");

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