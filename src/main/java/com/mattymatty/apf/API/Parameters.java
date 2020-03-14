package com.mattymatty.apf.API;

import com.mattymatty.apf.object.Position;

public interface Parameters {
    /**
     * @return the start position of current instance
     */
    Position getStartPosition();

    /**
     * @return maximum weight a path can have
     */
    double getMaxWeight();

    /**
     * @return maximum distance from start a Position can be
     */
    long getMaxDistance();

    /**
     * @return maximum number of steps a path can be
     */
    long getMaxSteps();

    /**
     * @return maximum number of total steps used to find a path
     */
    long getMaxTries();

}
