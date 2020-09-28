package com.mattymatty.apf.pathfinder;

import com.mattymatty.apf.AdvancedPathfinder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

class Pathfinder{

    public Pathfinder() {}

    Entity entity;

    List<Movement> movements = new ArrayList();

    public void setEntity(Entity entity){
        this.entity = entity;
    }

    public List<Movement> getMovements(){
        return movements;
    }

    public void addMovement(Movement move){
        movements.add(move);
    }

    public void addMovements(List<Movement> moves){
        movements.addAll(moves);
    }

    public FuturePath getPath(Location start, Location end){
        FuturePath result = new FuturePath();
        Thread pathThread = new Thread(()->run(start,end,result));
        result.runningThread = pathThread;

        pathThread.setName(String.format("Path-Thread: { %f, %f, %f } - { %f, %f, %f }",start.getX(),start.getY(),start.getZ(),end.getX(),end.getY(),end.getZ()));
        pathThread.setPriority(Thread.MAX_PRIORITY);
        pathThread.start();

        AdvancedPathfinder.QueueTask task = new AdvancedPathfinder.QueueTask(5,null);
        task.setTask((a)->{
            try {
                long initMicro = a;
                synchronized (result.lock) {
                    result.startMicro = initMicro;
                    result.lock.notifyAll();
                    result.lock.wait();
                }
                if(!result.isDone()){
                    AdvancedPathfinder.instance.addTask(task);
                }
            }catch (InterruptedException ex){
                result.cancel(true);
            }
        });
        AdvancedPathfinder.instance.addTask(task);
        return result;
    }


    private void run(Location start, Location end, FuturePath result){
        try {
            PriorityQueue<GraphPosition> pqueue = new PriorityQueue<>(Comparator.comparing(GraphPosition::getWeight));
            List<GraphPosition> clearList = new LinkedList<>();

            List<Movement> movementList = new ArrayList<>(movements);
            Entity currEntity = entity;

            GraphPosition startpos = new GraphPosition(start.getX(),start.getY(),start.getZ(),0);

            pqueue.add(startpos);
            clearList.add(startpos);

            while (!pqueue.isEmpty()) {
                GraphPosition curr = pqueue.poll();
                if(end.getX() == curr.getX() && end.getY() == curr.getY() && end.getY() == curr.getY()){
                    result.result = new Path(curr.path,start.getWorld());
                    result.isDone = true;
                    pqueue.clear();
                    for(GraphPosition pos : clearList){
                        pos.path.clear();
                    }
                    break;
                }

                for (Movement move : movementList){
                    if((System.nanoTime()/100)-result.startMicro>AdvancedPathfinder.maxTime){
                        synchronized (result.lock) {
                            result.lock.notifyAll();
                            result.lock.wait();
                        }
                    }
                    GraphPosition output = move.process(curr,currEntity);
                    if(output!=null){
                        pqueue.add(output);
                        clearList.add(output);
                    }
                }
            }
        }catch (InterruptedException ex){
            result.isCancelled = result.isDone = true;
        } finally {
            result.lock.notifyAll();
        }
    }


}
