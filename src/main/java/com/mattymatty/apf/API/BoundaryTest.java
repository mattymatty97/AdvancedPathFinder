package com.mattymatty.apf.API;

import com.mattymatty.apf.object.Position;


public interface BoundaryTest {
    /**
     * determines if {@param position} is inside the PathFind region
     * @param position  the Position to test
     * @param weight    the actual weight of the path
     * @param steps     how long the path is since now
     * @param params    the Parameters used to pathfind
     * @return  if false this current position will be discarded
     */
    boolean isInBoundary(Position position, double weight, int steps, Parameters params);
}
