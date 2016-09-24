package cs4532.lab3;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        BusStop busStop = new BusStop();
        Bus bus = new Bus(1);

        busStop.setCurrentBus(bus);
        addPassengers(busStop);

    }

    public static void addPassengers(BusStop busStop) {
        Passenger p;
        for (int i = 0; i < 70 ; i++) {
            busStop.addObserver((p = new Passenger(i)));
            p.start();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
