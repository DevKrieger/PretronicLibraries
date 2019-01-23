package net.prematic.libraries.tasking.intern;

import net.prematic.libraries.tasking.Task;
import net.prematic.libraries.tasking.TaskScheduler;
import net.prematic.libraries.utility.owner.ObjectOwner;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 02.09.18 13:15
 *
 */

public class PrematicTask implements Task {

    private int id;
    private Long delay, period;
    private boolean async, running;
    private TaskScheduler scheduler;
    private ObjectOwner owner;
    private Runnable runnable;

    public PrematicTask(int id,Long delay,Long period,TaskScheduler scheduler,ObjectOwner owner, Runnable runnable, Boolean async) {
        this.id = id;
        this.delay = delay;
        this.period = period;
        this.scheduler = scheduler;
        this.owner = owner;
        this.runnable = runnable;
        this.async = async;
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
    public boolean isRunning() {
        return this.running;
    }
    public TaskScheduler getScheduler() {
        return this.scheduler;
    }
    public ObjectOwner getOwner() {
        return this.owner;
    }
    public boolean isAsync() {
        return this.async;
    }
    public Runnable getRunnable() {
        return this.runnable;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    public void cancel(){
        setRunning(false);
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
                if(this.async) this.owner.getExecutor().execute(this.runnable);
                else this.runnable.run();
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
            if(this.period != null && this.period > 0L){
                try {
                    Thread.sleep(this.period);
                }catch(InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    cancel();
                }
            }else cancel();
        }
        cancel();
    }
}
