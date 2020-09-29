package com.mattymatty.apf.pathfinder;

import org.bukkit.entity.Entity;

public interface Movement {

    GraphPosition process(GraphPosition curr, Entity entity);

}
