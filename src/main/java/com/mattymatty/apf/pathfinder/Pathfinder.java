package com.mattymatty.apf.pathfinder;

import com.mattymatty.apf.API.Parameters;
import com.mattymatty.apf.object.Position;

public interface Pathfinder{

    void calculate();

    boolean isRunning();

    boolean hasPath();

    Position[] getPath();

    Position getStart();

    Position getEnd();

    void setParams(Parameters params);

    Parameters getParams();

    void addCallback(Runnable callback);

    Runnable removeCallback(Runnable callback);

    void free();

}
