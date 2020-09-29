package com.mattymatty.apf.pathfinder;

import com.mattymatty.apf.AdvancedPathfinder;

import java.util.Set;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FuturePath implements Future<Path> {

    PriorityQueue<GraphPosition> currQueue;
    Set<GraphPosition> visitedSet;

    long startMillis = -AdvancedPathfinder.maxTime;

    Path result;

    boolean isCancelled;

    boolean isDone;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        currQueue.clear();
        isCancelled = true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public Path get() throws InterruptedException, ExecutionException {
        return result;
    }

    @Override
    public Path get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return result;
    }
}
