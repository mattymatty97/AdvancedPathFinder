package com.mattymatty.apf.pathfinder.v1_15_R1;

import com.mattymatty.apf.pathfinder.MovementCheck;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;


public class GroundCheck implements MovementCheck{
    private Vec3D[] multipliers = new Vec3D[]{
            new Vec3D(1,0,0),
            new Vec3D(0,0,1),
            new Vec3D(1,0,1),
            new Vec3D(1,0,0.5),
            new Vec3D(0.5,0,1),
            new Vec3D(1,0,0.25),
            new Vec3D(0.25,0,1),
    };

    @Override
    public boolean isAllowed(double x, double y, double z, Entity entity) {
        net.minecraft.server.v1_15_R1.Entity MCentity = ((CraftEntity)entity).getHandle();
        AxisAlignedBB boundingBox = MCentity.getBoundingBox();
        Vec3D box = new Vec3D(boundingBox.b(),boundingBox.c(),boundingBox.d());
        Vec3D start = new Vec3D(x,y,z).add(-0.5*box.getX(),-0.1,-0.5*box.getZ());
        boolean pass = false;
        for(Vec3D mult : multipliers){
            MovingObjectPositionBlock res = MCentity.getWorld().rayTrace(new RayTrace(start,start.e(box.h(mult)), RayTrace.BlockCollisionOption.COLLIDER, RayTrace.FluidCollisionOption.ANY,null));
            if(res!= null && res.getType() != MovingObjectPosition.EnumMovingObjectType.MISS) {
                pass = true;
                break;
            }
        }
        return pass;
    }
}
