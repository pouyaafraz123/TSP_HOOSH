package com.projects.hoosh_tsp_project.tsp;

public class Vertex {
    public static int count = 1;
    private static long iCount;
    private long id;
    private String name;
    private double x;
    private double y;
    private double z;

    private Vertex() {
    }

    public Vertex(String name, double x, double y) {
        this(name, x, y, 0);
    }

    public Vertex(String name, double x, double y, double z) {
        this.id = ++iCount;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vertex getRandom(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        Vertex randomVertex = new Vertex();
        String name;
        double x;
        double y;
        double z;
        name = String.valueOf(count++);
        x = (maxX - minX) * Math.random() + minX;
        y = (maxY - minY) * Math.random() + minY;
        z = (maxZ - minZ) * Math.random() + minZ;
        randomVertex.setName(name);
        randomVertex.setX(x);
        randomVertex.setY(y);
        randomVertex.setZ(z);
        return randomVertex;
    }

    public String getName() {
        return name;
    }

    private void setName(String c) {
        this.name = c;
    }

    public double getX() {
        return x;
    }

    private void setX(double n) {
        this.x = n;
    }

    public double getY() {
        return y;
    }

    private void setY(double n) {
        this.y = n;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double n) {
        this.z = n;
    }

    public Double[] getCoordinates() {
        return new Double[]{this.x, this.y, this.z};
    }

    public String getInfo() {
        return this.name + ": " + this.x + "," + this.y + "," + this.z;
    }

    public double getDistance(Vertex v) {
        double nX_from = this.getX();
        double nY_from = this.getY();
        double nZ_from = this.getZ();

        double nX_to = v.getX();
        double nY_to = v.getY();
        double nZ_to = v.getZ();

        return Math.sqrt(Math.pow(nX_from - nX_to, 2) + Math.pow(nY_from - nY_to, 2) + Math.pow(nZ_from - nZ_to, 2));
    }
}