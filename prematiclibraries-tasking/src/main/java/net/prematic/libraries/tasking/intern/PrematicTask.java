package net.prematic.libraries.tasking.intern;

import net.prematic.libraries.tasking.Task;
import net.prematic.libraries.tasking.TaskOwner;
import net.prematic.libraries.tasking.TaskScheduler;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 13:15
 *
 */

public class PrematicTask implements Task {

    private int id;
    private Long delay, period;
    private Boolean running;
    private TaskScheduler scheduler;
    private TaskOwner owner;
    private Runnable runnable;

    public PrematicTask(int id,Long delay,Long period,TaskScheduler scheduler,TaskOwner owner, Runnable runnable) {
        this.id = id;
        this.delay = delay;
        this.period = period;
        this.scheduler = scheduler;
        this.owner = owner;
        this.runnable = runnable;
        this.running = false;
    }
    public int getID() {
        return this.id;
    }
    public Long getDelay() {
        return this.delay;
    }
    public Long getPeriod() {
        return this.period;
    }
    public Boolean isRunning() {
        return this.running;
    }
    public TaskScheduler getScheduler() {
        return this.scheduler;
    }
    public TaskOwner getOwner() {
        return this.owner;
    }
    public Runnable getRunnable() {
        return this.runnable;
    }
    public void setRunning(Boolean running) {
        this.running = running;
    }
    public void cancel(){
        this.scheduler.cancelTask(this);
    }
    public void run() {
        this.running = true;
        if(this.delay > 0){
            try {
                Thread.sleep(this.delay);
            }catch(InterruptedException exception) {
                Thread.currentThread().interrupt();
                cancel();
            }
        }
        while(this.running){
            try{
                this.runnable.run();
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
            if(this.period != null){
                try {
                    Thread.sleep(this.period);
                }catch(InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    cancel();
                }
            }
        }
        cancel();
    }
}
