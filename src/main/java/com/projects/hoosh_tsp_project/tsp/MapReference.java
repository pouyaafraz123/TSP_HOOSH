package com.projects.hoosh_tsp_project.tsp;

public class MapReference {
    double minX;
    double maxX;
    double minY;
    double maxY;
    double minZ;
    double maxZ;

    public MapReference() {
        this.minX = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxY = Double.MIN_VALUE;
        this.minZ = Double.MAX_VALUE;
        this.maxZ = Double.MIN_VALUE;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double n) {
        this.minX = n;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double n) {
        this.maxX = n;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double n) {
        this.minY = n;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double n) {
        this.maxY = n;
    }

    public double getMinZ() {
        return minZ;
    }

    public void setMinZ(double n) {
        this.minZ = n;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(double n) {
        this.maxZ = n;
    }

    public double getMapWidth() {
        return this.maxX - this.minX;
    }

    public double getMapHeight() {
        return this.maxY - this.minY;
    }

    public String getInfo() {
        String info = "";
        info += "P_min(" + String.format("%.8f", this.getMinX()) + " , " +
                String.format("%.8f", this.getMinY()) + " , " +
                String.format("%.8f", this.getMinZ()) + " )" + "\n";

        info += "P_max(" + String.format("%.8f", this.getMaxX()) + " , " +
                String.format("%.8f", this.getMaxY()) + " , " +
                String.format("%.8f", this.getMaxZ()) + " )" + "\n";
        return info;
    }
}
