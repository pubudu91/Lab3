package cs4532.lab3;

import java.util.ArrayList;
import java.util.Observable;

public class Bus {
    private int busId;
    private final int CAPACITY = 50;
    private ArrayList<Passenger> passengers = new ArrayList<>();

    public Bus(int id) {
        this.busId = id;
    }

    public boolean boardBus(Passenger p) {
        synchronized (this) {
            if (passengers.size() < CAPACITY)
                return passengers.add(p);
            else
                return false;
        }
    }

    public int getBusId() {
        return busId;
    }
}
