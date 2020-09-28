package com.mattymatty.apf.pathfinder;

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
        path.add(this);
        this.x=x;
        this.y=y;
        this.z=z;
        this.weight=weight;
    }

    public GraphPosition(double x, double y, double z, double weight) {
        this.path = new LinkedList<>();
        path.add(this);
        this.x=x;
        this.y=y;
        this.z=z;
        this.weight=weight;
    }
}
