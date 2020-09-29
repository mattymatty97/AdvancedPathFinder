package com.mattymatty.apf.pathfinder;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class Path {
    final List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    Path(List<GraphPosition> path, World world) {
        locations = new LinkedList<>();
        for(GraphPosition pos : path){
            locations.add(new Location(world,pos.getX(),pos.getY(),pos.getZ()));
        }
    }
}
