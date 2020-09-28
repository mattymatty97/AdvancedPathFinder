package com.mattymatty.apf;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Consumer;

public class AdvancedPathfinder extends JavaPlugin {

    public static AdvancedPathfinder instance;

    public static final long maxTime = 25000;

    private BukkitTask looper;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Objects.requireNonNull(this.getCommand("advancedpathfinder")).setExecutor(new Commands());
        Bukkit.getServer().getScheduler().runTaskTimer(this,this::looper,1,2);
    }

    public void addTask(QueueTask task){
        queuedTasks.add(task);
    }

    private final PriorityQueue<QueueTask> queuedTasks = new PriorityQueue<>();

    private void looper(){
        long act , micro = act = System.nanoTime()/1000;
        do{
            QueueTask task = queuedTasks.poll();
            if (task != null){
                task.accept(act);
            }
            act=System.nanoTime()/1000;
        }while (act-micro < maxTime);
    }

    public static class QueueTask implements Consumer<Long>, Comparable<QueueTask>{
        int priority;

        Consumer<Long> task;

        public QueueTask setTask(Consumer<Long> task) {
            this.task = task;
            return this;
        }

        public void accept(Long aLong){
            if(task != null)
                task.accept(aLong);
        }

        @Override
        public int compareTo(QueueTask o) {
            return Integer.compare(o.priority,this.priority);
        }

        public QueueTask(int priority, Consumer<Long> task) {
            this.task=task;
            this.priority = priority;
        }
    }

}
