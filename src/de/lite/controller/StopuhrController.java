package de.lite.controller;

import de.lite.model.TimerThread;
import de.lite.view.Stopuhr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StopuhrController {
    private Stopuhr stopuhr;
    private TimerThread timer;
    private Thread guiThread;
    private ExecutorService executor;
    private boolean running;
    private int rounds;
    private int actRound;

    public StopuhrController() {
        stopuhr = new Stopuhr(this);
        timer = new TimerThread();

        rounds = 0;
        actRound = 0;
    }

    public void startTimer() {
        actRound = 0;

        timer.startTimer();

        running = true;

        guiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        stopuhr.setTimeLabel(timer.getTime());
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        executor = Executors.newSingleThreadExecutor();
        executor.execute(guiThread);
    }

    public String startNewRound() {
        return timer.startNewRound();
    }

    public String stopTimer() {
        String _temp = timer.stopTimer();

        running = false;

        try {
            guiThread.join();

            executor.shutdown();
            executor.awaitTermination(5000, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return _temp;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getActRound() {
        return actRound;
    }

    public void resetActRound() {
        this.actRound = 0;
    }

    public void incrementActRounds() {
        actRound++;
    }

    public void showGUI() {
        stopuhr.show();
    }
}
