import clock.AlarmClockEmulator;
import clock.io.*;
import clock.io.ClockInput.UserInput;


import java.util.concurrent.Semaphore;
import clock.io.ClockInput;
import clock.io.ClockOutput;

public class ClockMain {

    public static void main(String[] args) throws InterruptedException {

        //Clock Emulator
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput in = emulator.getInput();
        ClockOutput out = emulator.getOutput();

        //Semaphore till alarm
         Semaphore semaphorealarm= new Semaphore(0);

        //Monitor
        ClockMonitor clockMonitor=new ClockMonitor(0,0,0,out,in,semaphorealarm);

        //Threads
        ClockTicker ticker = new ClockTicker(out,in,clockMonitor);
        UserInputHandler userInputHandler = new UserInputHandler( clockMonitor,in,out);
        AlarmClock alarmClock=new AlarmClock(out,semaphorealarm);

        ticker.start();
        userInputHandler.start();
        alarmClock.start();








    }
}

