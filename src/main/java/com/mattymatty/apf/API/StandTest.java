package com.mattymatty.apf.API;

import com.mattymatty.apf.object.Position;

public interface StandTest {
    /**
     * determines if we can Stand on the current {@param position}
     * @param position  the current Position
     * @param params    the Parameters used to pathfind
     * @return  true if we can stay in the current {@param position}, false otherwise
     */
    boolean canStandOn(Position position, Parameters params);
}
