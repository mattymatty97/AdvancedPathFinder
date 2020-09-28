package com.mattymatty.apf.pathfinder;

import org.bukkit.entity.Entity;

public interface MovementCheck {
    boolean isAllowed(double x, double y, double z, Entity entity);
}
