package de.lite.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TimerThread {
    private ExecutorService executor;
    private Thread timer;


    private SimpleDateFormat formater;
    private AtomicLong startTime;
    private AtomicLong roundTime;
    private AtomicLong actTime;
    private AtomicBoolean running;

    public TimerThread() {
        formater = new SimpleDateFormat("mm:ss.SSS");

        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running.get()) {
                    try {
                        actTime.set(System.currentTimeMillis() - startTime.get());

                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void startTimer() {
        startTime = new AtomicLong(System.currentTimeMillis());
        roundTime = new AtomicLong(0L);
        actTime = new AtomicLong(0L);
        running = new AtomicBoolean(true);

        executor = Executors.newSingleThreadExecutor();
        executor.execute(timer);
    }

    public String getRoundTime() {
        long _tempTime = actTime.get();

        String roundTimeString = formater.format(new Date(_tempTime - roundTime.get()));
        roundTime.set(_tempTime);

        return roundTimeString;
    }

    public String stopTimer() {
        running.set(false);

        try {
            timer.join();

            executor.shutdown();
            executor.awaitTermination(5000, TimeUnit.SECONDS);

            return formater.format(new Date(actTime.get()));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String getTime() {
        return formater.format(new Date(actTime.get()));
    }
}
