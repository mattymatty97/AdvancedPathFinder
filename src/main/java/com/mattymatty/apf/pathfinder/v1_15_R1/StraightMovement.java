package com.mattymatty.apf.pathfinder.v1_15_R1;

import com.mattymatty.apf.pathfinder.GraphPosition;
import com.mattymatty.apf.pathfinder.Movement;
import net.minecraft.server.v1_15_R1.Chunk;
import net.minecraft.server.v1_15_R1.Vec3D;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class StraightMovement implements Movement {
    static final BoundingBoxCheck BOUNDING_BOX_CHECK = new BoundingBoxCheck();
    static final GroundCheck GROUND_CHECK = new GroundCheck();
    double dx;
    double dy;
    double dz;

    public StraightMovement(double dx, double dy, double dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public Map<String,Double> cost_Map = new HashMap<>();

    public Map<String, Double> getCost_Map() {
        return cost_Map;
    }

    public StraightMovement setCost_Map(Map<String, Double> cost_Map) {
        this.cost_Map = cost_Map;
        return this;
    }

    @Override
    public GraphPosition process(GraphPosition curr, Entity entity) {
        double x,y,z;
        x = curr.getX();
        y = curr.getY();
        z = curr.getZ();

        if(BOUNDING_BOX_CHECK.isAllowed(x+dx,y+dy,z+dz,entity) &&
                GROUND_CHECK.isAllowed(x+dx,y+dy,z+dz,entity) &&
                BOUNDING_BOX_CHECK.isAllowed(x+0.5*dx,y+0.5*dy,z+0.5*dz,entity) &&
                GROUND_CHECK.isAllowed(x+0.5*dx,y+0.5*dy,z+0.5*dz,entity)
        ){
            GraphPosition ret = new GraphPosition(curr.getPath(),x+dx,y+dy,z+dz,
                    curr.getWeight()+getCost(curr,entity)
            );
            return ret;
        }

        return null;
    }

    private double getCost(GraphPosition act,Entity entity){
        Vec3D curr = new Vec3D(act.getX(),act.getY(),act.getZ());
        Vec3D end = curr.add(dx,dy,dz);
        double distance = new Vec3D(dx,0,dz).f();
        Chunk chunk = ((CraftEntity)entity).getHandle().getWorld().getChunkAt((int)end.getX()/16,(int)end.getZ()/16);
        String endBlock = chunk.getBlockData((int)end.getX(),((int)end.getY())-1,(int)end.getZ()).getBlock().k();
        String currBlock = chunk.getBlockData((int)curr.getX(),((int)curr.getY())-1,(int)curr.getZ()).getBlock().k();
        double cost = (cost_Map.getOrDefault(endBlock,100d) + cost_Map.getOrDefault(currBlock,100d))*distance;
        cost+= dy*dy;
        return cost;
    }
}
