package lift;

class Monitor {
    private int[] toEnter, toExit;
    private int passengersInside;
    private  int walking;
    private int currentFloor;
    private int maxCapacity;
    private boolean moving;
    private boolean up;

    private int Inside;

    private LiftView lv;

    private boolean doorsOpen = false;

    private Passenger passenger;

    public Monitor(int maxCapacity, LiftView view) {
        this.maxCapacity = maxCapacity;
        this.passengersInside = 0;
        this.currentFloor = 0;
        this.moving = false;
        this.up=false;
        this.toEnter = new int[7];
        this.toExit = new int[7];
        this.Inside=0;
        this.lv=view;
        this.walking=0;

    }

    // Method called by passengers to enter the lift
    public synchronized void enterLift(int fromFloor, int toFloor, Passenger passenger) {
        toEnter[fromFloor]++;
        lv.showDebugInfo(toEnter, toExit);
        while (!(currentFloor == fromFloor && Inside<maxCapacity && doorsOpen)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Inside++;
        toEnter[fromFloor]--;
        toExit[toFloor]++;
        lv.showDebugInfo(toEnter, toExit);
        walking++;

        notifyAll();
    }

    public synchronized void processPassengers(int floor) {
        currentFloor = floor;
        notifyAll();
    }

    public synchronized void walking(){
        walking--;
        notifyAll();
    }

    public synchronized boolean getToExit(){
        for(int nbr : toEnter){
            if(nbr > 0){
                return true;
            }
        }
        return false;
    }

    public synchronized void moveCondition(int toFloor) throws InterruptedException {
        while (toExit[toFloor] > 0 || (toEnter[toFloor] > 0 && Inside < 4  ||  walking>0)) {
            lv.showDebugInfo(toEnter, toExit);
            if (!doorsOpen) {
                System.out.println("Open doors on floor " + toFloor);
                lv.openDoors(toFloor);

                doorsOpen = true;
                notifyAll();
            }
            wait();

        }

        if (doorsOpen) {
            System.out.println("Close doors on floor " + toFloor);
            lv.closeDoors();
            doorsOpen = false;
        }

        notifyAll();
    }




    public synchronized void exitLift(int floor, Passenger passenger) {
        while (!(currentFloor == floor && doorsOpen )) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Inside--;
        toExit[floor]--;
        walking++;
        lv.showDebugInfo(toEnter, toExit);

        notifyAll();
    }
}

