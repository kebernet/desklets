package org.joshy.countdown;

public class CountdownUpdater implements Runnable {
    public CountdownUpdater(CountdownMain main){
        this.main= main;
    }
    private CountdownMain main;
    public void run() {
        try {
            while(true) {
                main.updateTime();
                Thread.currentThread().sleep(1000*60*60*1); // update every hour
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}

