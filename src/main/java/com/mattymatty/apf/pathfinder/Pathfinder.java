package com.mattymatty.apf.pathfinder;

import com.mattymatty.apf.AdvancedPathfinder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

public class Pathfinder{

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

    public FuturePath getPath(Location start, Location end, Consumer<FuturePath> callback){
        FuturePath result = new FuturePath();
        /*Thread pathThread = new Thread(()->run(start,end,result));
        result.runningThread = pathThread;

        pathThread.setName(String.format("Path-Thread: { %f, %f, %f } - { %f, %f, %f }",start.getX(),start.getY(),start.getZ(),end.getX(),end.getY(),end.getZ()));
        pathThread.setPriority(Thread.MAX_PRIORITY);
        pathThread.start();*/

        result.currQueue = new PriorityQueue<>(Comparator.comparing(GraphPosition::getWeight));
        result.visitedSet = new HashSet<>();

        GraphPosition startpos = new GraphPosition(start.getX(),start.getY(),start.getZ(),0);

        result.currQueue.add(startpos);
        result.visitedSet.add(startpos);


        AdvancedPathfinder.QueueTask task = new AdvancedPathfinder.QueueTask(5,null);
        task.setTask((a)->{
            try {
                result.startMillis = a;
                run2(start,end,result);
                if(!result.isDone()){
                    AdvancedPathfinder.instance.addTask(task);
                }else{
                    callback.accept(result);
                }
            }catch (Exception ex){
                result.cancel(true);
            }
        });
        AdvancedPathfinder.instance.addTask(task);
        return result;
    }

/*
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
        }catch (Exception ex){
            result.isCancelled = result.isDone = true;
        } finally {
            synchronized (result.lock) {
                result.lock.notifyAll();
            }
        }
    }
*/

    private void run2(Location start, Location end, FuturePath result){
        try {
            PriorityQueue<GraphPosition> pqueue = result.currQueue;
            Set<GraphPosition> visitedSet = result.visitedSet;

            List<Movement> movementList = new ArrayList<>(movements);
            Entity currEntity = entity;

            while (!pqueue.isEmpty() && (System.currentTimeMillis()-result.startMillis <AdvancedPathfinder.maxTime)) {
                GraphPosition curr = pqueue.poll();
                assert curr != null: "got null from queue.poll()";
                if(new Vector(end.getX(),end.getY(),end.getZ()).distance(new Vector(curr.getX(),curr.getY(),curr.getZ()))<0.7){
                    result.result = new Path(curr.path,start.getWorld());
                    result.isDone = true;
                    pqueue.clear();
                    for(GraphPosition pos : visitedSet){
                        pos.path.clear();
                    }
                    visitedSet.clear();
                    break;
                }

                for (Movement move : movementList){
                    GraphPosition output = move.process(curr,currEntity);
                    if(output!=null){
                        if(!visitedSet.contains(output)) {
                            double distance = new Vector(output.getX(),output.getY(),output.getZ()).distance(end.toVector());
                            output.weight+=distance;
                            pqueue.add(output);
                            visitedSet.add(output);
                        }else{
                            output.path.clear();
                        }
                    }
                }
            }

            if(pqueue.isEmpty()){
                result.isDone=true;
            }
        }catch (Exception ex){
            result.isCancelled = result.isDone = true;
        }
    }

}
