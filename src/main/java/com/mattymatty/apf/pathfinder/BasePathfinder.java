package com.mattymatty.apf.pathfinder;

import com.mattymatty.apf.API.BoundaryTest;
import com.mattymatty.apf.API.Parameters;
import com.mattymatty.apf.API.StandTest;
import com.mattymatty.apf.object.IntegerNode;
import com.mattymatty.apf.object.IntegerPosition;
import com.mattymatty.apf.object.ItemReference;
import com.mattymatty.apf.object.Position;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BasePathfinder implements Pathfinder {

    Parameters params;

    Position start;

    Position end;

    boolean running;

    boolean completed;

    Position[] path;

    public BasePathfinder(@NotNull Parameters params, @NotNull Position start,@NotNull Position end) {
        if(params==null || start==null || end == null)
            throw new NullPointerException("the fields cannot be null");
        this.params = params;
        this.start = start;
        this.end = end;
    }

    public BasePathfinder(@NotNull Position start,@NotNull Position end,@NotNull World world) {
        if(start==null || end == null || world==null )
            throw new NullPointerException("the fields cannot be null");
        this.params = new InnerParam(start,world);
        this.start = start;
        this.end = end;
    }

    @Override
    public void calculate() {
        if(!running){
            completed=false;
            running=true;
            path = null;
            free();

            if(params.getCallbacks().standTest.canStandOn(start,params) && params.getCallbacks().standTest.canStandOn(end,params)){
                pqueue.add(new IntegerNode(start));
                _calculate();
            }else{
                completed = true;
                running = false;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean hasPath() {
        return completed;
    }

    @Override
    @Nullable
    public Position[] getPath() {
        return path;
    }

    @Override
    @NotNull
    public Position getStart() {
        return start;
    }

    @Override
    @NotNull
    public Position getEnd() {
        return end;
    }

    @Override
    public void setParams(Parameters params) {
        this.params = params;
    }

    @Override
    @NotNull
    public Parameters getParams() {
        return params;
    }

    LinkedList<Runnable> callbacks = new LinkedList<>();

    @Override
    public void addCallback(Runnable callback) {
        callbacks.add(callback);
    }

    @Override
    public Runnable removeCallback(Runnable callback) {
        if(callbacks.remove(callback)){
            return callback;
        }else
            return null;
    }

    @Override
    public void free() {
        if(!running && completed) {
            leafs.forEach(l -> {
                for (Position pos : l.getHistory()) {
                    if (pos instanceof ItemReference) {
                        ((ItemReference) pos).free();
                    }
                }
                l.free();
            });
            leafs.clear();
        }
    }

    private class InnerParam implements Parameters{

        Position startPosition;

        World world;

        @Override
        public World getWorld() {
            return world;
        }

        @Override
        public Position getStartPosition() {
            return startPosition;
        }

        @Override
        public double getMaxWeight() {
            return Double.MAX_VALUE;
        }

        @Override
        public long getMaxDistance() {
            return 32;
        }

        @Override
        public long getMaxSteps() {
            return Integer.MAX_VALUE;
        }

        @Override
        public long getMaxTries() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Callbacks getCallbacks() {
            Callbacks cb = new Callbacks();
            cb.standTest = new StandTest() {
                @Override
                public boolean canStandOn(Position position, Parameters params) {
                    Block block = params.getWorld().getBlockAt((int)position.getX(),(int)position.getY(),(int)position.getZ());
                    if(block.isEmpty()){
                        Block up = block.getRelative(BlockFace.UP);
                        if(up.isEmpty()){
                            Block down = block.getRelative(BlockFace.DOWN);
                            if(!down.isPassable() && !down.isEmpty() && !down.isLiquid())
                                return true;
                        }
                    }


                    return false;
                }
            };

            return cb;
        }

        public InnerParam(Position startPosition, World world) {
            this.startPosition = startPosition;
            this.world = world;
        }
    }

    PriorityQueue<IntegerNode> pqueue = new PriorityQueue<>(Comparator.comparing(IntegerNode::getWeight));
    LinkedList<IntegerNode> leafs = new LinkedList<>();

    private void _calculate(){
        while(!pqueue.isEmpty()) {
            IntegerNode act = pqueue.poll();
            if (act.equals(end)) {
                path = act.getHistory();
                running = false;
                completed = true;
                callbacks.forEach(Runnable::run);
                return;
            }


            if(params.getCallbacks().boundaryTest.isInBoundary(act,act.getWeight(),act.getHistory().length,params) &&
                    params.getCallbacks().standTest.canStandOn(act,params)){
                Position[] list = params.getCallbacks().nextSteps.nextMovements(act, params);
                if(list.length>0) {
                    for (Position next : list) {
                        double cost = params.getCallbacks().movementCost.movementCost(act, next, params);
                        Position[] path = act.getHistory();
                        path = Arrays.copyOf(path, path.length + 1);
                        path[path.length - 1] = next;
                        IntegerNode nextNode = new IntegerNode(
                                (int) act.getX(), (int) act.getY(), (int) act.getZ(),
                                path,
                                act.getWeight() + cost
                        );
                        pqueue.add(nextNode);
                    }
                    act.free();
                }else{
                    leafs.add(act);
                }
            }else{
                leafs.add(act);
            }
        }
        path = null;
        running = false;
        completed = true;
    }

}
