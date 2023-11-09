package clock.io;

import java.util.concurrent.Semaphore;

public class AlarmClock extends Thread{
    private ClockOutput out;
    private Semaphore semaphorealarm;
    public AlarmClock(ClockOutput out, Semaphore semaphore) {
        this.out = out;
        this.semaphorealarm=semaphore;

    }


    public void run() {
        try {
            while(true) {
                semaphorealarm.acquire();
                long startTime = System.currentTimeMillis(); // Record the start time

                // Ring the alarm for 20 seconds
                while (System.currentTimeMillis() - startTime < 20000) {
                    out.alarm(); // Trigger visual alarm
                    Thread.sleep(1000); // Wait for one second
                }

                //semaphorealarm.release(); // Release the semaphore after 20 seconds
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
