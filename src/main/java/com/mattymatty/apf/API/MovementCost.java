package com.mattymatty.apf.API;

import com.mattymatty.apf.object.Position;

public interface MovementCost {
    /**
     * determines the cost ( weight ) of the current move
     * @param start     the actual Position
     * @param end       the destination Position
     * @param params    the Parameters used to pathfind
     * @return  the cost of the move ( should be positive and non zero )
     */
    double movementCost(Position start, Position end,Parameters params);
}
