package lift;

public class Main {
    public static void main(String[] args) {

        final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;

        LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
        Monitor monitor = new Monitor(MAX_PASSENGERS, view);

        PassengerThread[] passengers = new PassengerThread[20]; // Create 20 passenger threads

        for (int i = 0; i < passengers.length; i++) {
            passengers[i] = new PassengerThread(view, monitor); // Create a new passenger thread with LiftView and Monitor
        }

        LiftThread lift = new LiftThread(view, monitor); // Create the lift thread with LiftView and Monitor

        for (PassengerThread passenger : passengers) {
            passenger.start(); // Start passenger threads
        }

        lift.start(); // Start the lift thread

        try {
            for (PassengerThread passenger : passengers) {
                passenger.join();
            }
            lift.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
