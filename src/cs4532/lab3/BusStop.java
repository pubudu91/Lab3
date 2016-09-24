package cs4532.lab3;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by pubudu on 9/24/16.
 */
public class BusStop extends Observable {
    //    private ArrayList<Passenger> passengers = new ArrayList<>();
    private Bus currentBus;

//    public void addPassenger(Passenger passenger) {
//        passengers.add(passenger);
//    }

    public void setCurrentBus(Bus currentBus) {
        this.currentBus = currentBus;
        setChanged();
        notifyObservers();
    }

    public Bus getCurrentBus() {
        return currentBus;
    }

    public void addObserver(Observer ob) {
        super.addObserver(ob);

        if(ob instanceof Passenger) {
            ob.update(this, null);
        }
    }
}
