package lift;

class PassengerThread extends Thread {
    private LiftView view;
    private Monitor monitor;

    public PassengerThread(LiftView view, Monitor monitor) {
        this.view = view;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            // Generate a new passenger with random start and destination floors
            Passenger passenger = view.createPassenger();
            int fromFloor = passenger.getStartFloor();
            int toFloor = passenger.getDestinationFloor();

            passenger.begin(); // walk in (from left)

            // Synchronize with the monitor to enter the lift
            monitor.enterLift(fromFloor, toFloor,passenger);
            passenger.enterLift();
            monitor.walking();

            monitor.exitLift(toFloor, passenger);
            passenger.exitLift();
            monitor.walking();

            passenger.end(); // walk out (to the right)
        }
    }
}

