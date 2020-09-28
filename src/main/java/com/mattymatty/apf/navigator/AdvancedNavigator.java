package com.mattymatty.apf.navigator;

import net.citizensnpcs.api.ai.*;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class AdvancedNavigator implements Navigator {
    @Override
    public void cancelNavigation() {

    }

    @Override
    public NavigatorParameters getDefaultParameters() {
        return null;
    }

    @Override
    public EntityTarget getEntityTarget() {
        return null;
    }

    @Override
    public NavigatorParameters getLocalParameters() {
        return null;
    }

    @Override
    public NPC getNPC() {
        return null;
    }

    @Override
    public PathStrategy getPathStrategy() {
        return null;
    }

    @Override
    public Location getTargetAsLocation() {
        return null;
    }

    @Override
    public TargetType getTargetType() {
        return null;
    }

    @Override
    public boolean isNavigating() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void setPaused(boolean paused) {

    }

    @Override
    public void setTarget(Entity target, boolean aggressive) {

    }

    @Override
    public void setTarget(Iterable<Vector> path) {

    }

    @Override
    public void setTarget(Location target) {

    }
}
