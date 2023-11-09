package clock.io;

import java.util.concurrent.Semaphore;
public class ClockMonitor {
    private ClockOutput out;
    private Semaphore semaphoreticker;
    private Semaphore semaphorealarm;
    private int hour, second, minute;
    private ClockInput in;

    private int alarmHour;

    private int alarmMinute;
    private int alarmSecond;

    private boolean alarmEnabled;


    public ClockMonitor(int hour, int minute, int second, ClockOutput out, ClockInput in, Semaphore semaphorealarm) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.out = out;
        semaphoreticker = new Semaphore(1);
       this.semaphorealarm = semaphorealarm;
        this.in = in;
        this.alarmHour = 0;
        this.alarmMinute = 0;
        this.alarmSecond = 0;


    }

    public synchronized void setTime(int h, int m, int s) throws InterruptedException {
        semaphoreticker.acquire();
        this.hour = h;
        this.minute = m;
        this.second = s;
        out.displayTime(h, m, s);
        semaphoreticker.release();
    }

    public synchronized void clockTicker() throws InterruptedException {
        synchronized (this){
            int hour = getHour();
            int minute = getMinute();
            int second = getSecond() + 1;

            if (second >= 60) {
                second = 0;
                minute++;
                if (minute >= 60) {
                    minute = 0;
                    hour++;
                    if (hour >= 24) {
                        hour = 0;
                    }
                }
            }
            try {
                setTime(hour, minute, second);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            checkAndTriggerAlarm(hour, minute, second);
        }

    }


    void checkAndTriggerAlarm(int hour, int minute, int second) throws InterruptedException {
        //semaphoreticker.acquire();
        int alarmHour = getAlarmHour();
        int alarmMinute = getAlarmMinute();
        int alarmSecond = getAlarmSecond();
     ///   semaphoreticker.release();


        if (isAlarmEnabled() && hour == alarmHour && minute == alarmMinute && second == alarmSecond) {
            semaphorealarm.release();
        }
    }

    //private void startAlarm() {
      //  AlarmClock alarmThread = new AlarmClock(out);
    //    alarmThread.start();
   // }

    public int getAlarmSecond() {
        return alarmSecond;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public void setAlarmSecond(int alarmSecond) {
        this.alarmSecond = alarmSecond;
    }

    public boolean isAlarmEnabled() {
        return alarmEnabled;
    }

    public void setAlarmEnabled(boolean alarmEnabled) {
        this.alarmEnabled = alarmEnabled;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public synchronized int getHour() {
        return hour;
    }

    public synchronized void setHour(int hour) {
        this.hour = hour;
    }

    public synchronized int getSecond() {
        return second;
    }

    public synchronized void setSecond(int second) {
        this.second = second;
    }

    public synchronized int getMinute() {
        return minute;
    }

    public synchronized void setMinute(int minute) {
        this.minute = minute;
    }

}
 //** WARNING: your call to AlarmClock.displayTime() was severely delayed
//   (34001 ms after the last call). AlarmClock.alarm() was called between,
//   and that likely has something to do with it:
//
//      displayTime() was called at time         t
//      alarm()       was called at time         t + 20991, took 393 ms to run
//      displayTime() was called again at time   t + 34001
//
//   It seems that your clock drift calculation did not compensate for
//   the 393 ms execution time of alarm(). You can probably see the clock
//   rate and/or update interval change too (temporarily).
//
//** HINT: look at the loop where you compensate for clock drift, and locate
//   the calls to displayTime() and alarm(). It is possible that your loop
//   calls a monitor method, which in turn calls these methods.
//
//   1. Consider the order between the calls: is displayTime() called before
//      alarm(), or the other way around? Can this order explain the problem?
//      (Think about what happens when alarm() takes long time to run.)
//
//   2. Review the loop where you compensate for clock drift. Think about the
//      case when alarm() is called the first time, taking about 300-400 ms
//      to run. Will the next Thread.sleep() call be correspondingly shorter?
//      If not, then the clock rate will be affected in precisely this way.