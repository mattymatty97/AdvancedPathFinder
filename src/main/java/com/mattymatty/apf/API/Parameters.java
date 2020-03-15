package com.mattymatty.apf.API;

import com.mattymatty.apf.object.Position;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.bukkit.World;

public interface Parameters {

    /**
     * @return the Bukkit world we're pathFinding in
     */
    World getWorld();
    /**
     * @return the start position of current instance
     */
    Position getStartPosition();

    /**
     * @return maximum weight a path can have
     */
    double getMaxWeight();

    /**
     * @return maximum distance from start a Position can be, (only X Z)
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

    /**
     * @return the actual pathfind callbacks
     */
    Callbacks getCallbacks();


    class Callbacks {
        public BoundaryTest boundaryTest;

        public MovementCost movementCost;

        public NextSteps nextSteps;

        public StandTest standTest;
    }

}
