package clock.io;

import clock.AlarmClockEmulator;
import java.util.concurrent.Semaphore;

public class UserInputHandler extends Thread {
    private ClockMonitor clockMonitor;
    private  ClockInput in;
    private ClockOutput out;

    private int alarmHours;
    private int alarmMinutes;
    private int alarmSeconds;
    private boolean alarmEnabled;


    public UserInputHandler( ClockMonitor clockMonitor, ClockInput in, ClockOutput out) {
        this.clockMonitor = clockMonitor;
        this.in=in;
        this.out=out;

        this.alarmHours = 0;
        this.alarmMinutes = 0;
        this.alarmEnabled = false;

    }

    public void run() {
        try {
            while (true) {

                in.getSemaphore().acquire();
                // Wait until input data is available

                ClockInput.UserInput userInput= in.getUserInput();
                Choice choice= userInput.choice();

                if (choice == Choice.SET_TIME) {
                    //   synchronized (out) {
                        int h = userInput.hours();
                        int m = userInput.minutes();
                        int s = userInput.seconds();

                        // Update the clock time
                        clockMonitor.setTime(h, m, s);

                }else if (choice == Choice.SET_ALARM) {
                    // Update the alarm time
                    int h = userInput.hours();
                    int m = userInput.minutes();
                    int s = userInput.seconds();
                    //varför behövs dessa
                    clockMonitor.setAlarmHour(h);
                    clockMonitor.setAlarmMinute(m);
                    clockMonitor.setAlarmSecond(s);
                   // clockMonitor.checkAndTriggerAlarm(h,m,s);
                  //  alarmHours = h;
                   // alarmMinutes = m;
                   // alarmSeconds=s;
                   // clockMonitor.setAlarmSecond(alarmSeconds);
                   // clockMonitor.setAlarmMinute(alarmMinutes);
                   // clockMonitor.setAlarmHour(alarmHours);

                } else if (choice == Choice.TOGGLE_ALARM) {
                    // Toggle alarm status
                    alarmEnabled = !alarmEnabled;
                    clockMonitor.setAlarmEnabled(alarmEnabled);
                    out.setAlarmIndicator(alarmEnabled);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

}
