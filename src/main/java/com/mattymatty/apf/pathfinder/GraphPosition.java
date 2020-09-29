package com.mattymatty.apf.pathfinder;

import org.bukkit.util.Vector;

import java.util.LinkedList;

public class GraphPosition {
    double x;
    double y;
    double z;

    double weight;

    LinkedList<GraphPosition> path;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getWeight() {
        return weight;
    }

    public LinkedList<GraphPosition> getPath() {
        return path;
    }

    public GraphPosition(LinkedList<GraphPosition> path, double x, double y, double z, double weight) {
        this.path = new LinkedList<>(path);
        this.x=x;
        this.y=y;
        this.z=z;
        this.weight=weight;
        this.path.add(this);
    }

    public GraphPosition(double x, double y, double z, double weight) {
        this.path = new LinkedList<>();
        this.x=x;
        this.y=y;
        this.z=z;
        this.weight=weight;
        this.path.add(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GraphPosition))
            return super.equals(obj);
        else{
            GraphPosition o1 = (GraphPosition)obj;
            return new Vector(o1.getX(),o1.getY(),o1.getZ()).distance(new Vector(this.getX(),this.getY(),this.getZ()))<0.1;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
        hash = 19 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
        return hash;
    }
}
