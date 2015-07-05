package com.thistech.vexdashboard.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brent on 7/5/15.
 */
public class StatusInsertSimulator {
    Timer timer;

    public StatusInsertSimulator(int seconds) {
        timer = new Timer();
        timer.schedule(new InsertionTask(), seconds*1000);
    }

    class InsertionTask extends TimerTask {
        public void run() {
            System.out.format("Time's up!%n");
            timer.cancel(); //Terminate the timer thread
        }
    }

    public static void main(String args[]) {
        new StatusInsertSimulator(5);
        System.out.format("Task scheduled.%n");
    }
}
