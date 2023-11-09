package lift;

class LiftThread extends Thread {
    private LiftView view;
    private Monitor monitor;

    private boolean passengerStartedWalking;

    public LiftThread(LiftView view, Monitor monitor) {
        this.view = view;
        this.monitor = monitor;
        this.passengerStartedWalking=false;
    }

    @Override
    public void run() {
        int currentFloor = 0; // Initialize at the first floor
        boolean goingUp = true;

        while (true) {
            if(monitor.getToExit()){

            // Move the lift
            int toFloor=goingUp ? currentFloor + 1 : currentFloor - 1;

            view.moveLift(currentFloor, toFloor);

            monitor.processPassengers(toFloor);

            try {
                monitor.moveCondition(toFloor);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Wait for 3 seconds
          /*  synchronized (this) {
                try {
                    wait(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            // Close doors before moving again
           // view.closeDoors();

            if (goingUp) {
                currentFloor++;
                if (currentFloor == 6) { // If at top floor, change direction
                    goingUp = false;
                }
            } else {
                currentFloor--;
                if (currentFloor == 0) { // If at bottom floor, change direction
                    goingUp = true;
                }
            }

            // Notify to wake up the thread after the wait
            synchronized (this) {
                notify();
            }
        }
        }
    }

}
