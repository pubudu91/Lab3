package cs4532.lab3;

import java.util.Observable;
import java.util.Observer;

public class BusStop extends Observable {
    private Bus currentBus; // The bus currently at the bus stop

    // A new bus arrives at the bus stop. Or, null is set if the current bus is departing
    public void arriveAtBusStop(Bus currentBus) {
        this.currentBus = currentBus;
        setChanged();
        notifyObservers();
    }

    public void departFromBusStop() {
        this.currentBus = null;
        setChanged();
        notifyObservers();
    }

    public Bus getCurrentBus() {
        return currentBus;
    }

    // A new passenger arrives at the bus stop
    public void addObserver(Observer ob) {
        super.addObserver(ob);

        if (ob instanceof Passenger) {
            ob.update(this, null); // Notify the passenger about the current bus at the bus stop
        }
    }
}
