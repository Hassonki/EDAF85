package clock.io;

public class ClockTicker extends Thread {
    private ClockMonitor clockMonitor;
    private ClockInput in;
    private ClockOutput out;

    public ClockTicker(ClockOutput out,ClockInput in, ClockMonitor clockMonitor) {
        this.clockMonitor = clockMonitor;
        this.in=in;
        this.in=in;
    }

    public void run() {
       long t, diff ;
       t=System.currentTimeMillis();
        try {
            while (true) {
                // Update the clock time every second

                clockMonitor.clockTicker();
                 t += 1000;
                 diff = t - System.currentTimeMillis();

                if(diff>0)
                {
                    Thread.sleep(diff);
                }else{
                    Thread.sleep(1000);
                }

                // Update the clock time

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}