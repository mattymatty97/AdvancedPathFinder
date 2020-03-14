package com.mattymatty.apf.API;

import com.mattymatty.apf.object.Position;

public interface NextSteps {
    /**
     * retrieves an array of Positions reachable with one step from {@param start}
     * @param start     the current Position
     * @param params    the Parameters used to pathfind
     * @return  an array containing the list of Positions to be checked after {@param start}
     */
    Position[] nextMovements(Position start,Parameters params);
}
