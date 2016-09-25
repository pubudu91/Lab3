package cs4532.lab3;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        BusStop busStop = new BusStop();
        Bus bus = new Bus(1);
        Bus bus2 = new Bus(2);


        addPassengers(busStop);
        busStop.arriveAtBusStop(bus);
        Thread.sleep(5000);
        busStop.departFromBusStop();
        Thread.sleep(1000);
        busStop.arriveAtBusStop(bus2);
    }

    public static void addPassengers(BusStop busStop) {
        Passenger p;
        for (int i = 0; i < 70; i++) {
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
