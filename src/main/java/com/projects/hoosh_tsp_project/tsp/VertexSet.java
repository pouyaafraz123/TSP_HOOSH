package com.projects.hoosh_tsp_project.tsp;

import java.util.ArrayList;
import java.util.Collections;

public class VertexSet {
    private static long count;
    private final long id;
    public MapReference map;
    private ArrayList<Vertex> Set = new ArrayList<>();

    public VertexSet() {
        this.id = ++count;
    }

    public VertexSet(Vertex... vertices) {
        this.id = ++count;
        Collections.addAll(this.Set, vertices);
    }

    public VertexSet(ArrayList<Vertex> aList) {
        this.id = ++count;
        this.Set = aList;
    }

    public void addVertex(Vertex v) {
        this.Set.add(v);
    }

    public Vertex getVertex(int index) {
        return this.Set.get(index);
    }

    public int getNumberOfVertices() {
        return this.Set.size();
    }

    public String getInfo() {
        StringBuilder info = new StringBuilder();
        for (Vertex vertex : Set) {
            info.append(vertex.getInfo()).append("\n");
        }
        return info.toString();
    }

    public double getTotalDistance(Path p) {
        double totalDistance = 0;
        int max = p.getLength() - 1;
        int from;
        int to;
        for (int i = 0; i < max; i++) {
            from = p.get(i);
            to = p.get(i + 1);
            totalDistance += this.Set.get(from).getDistance(this.Set.get(to));
        }
      //  totalDistance += this.Set.get(max).getDistance(this.Set.get(0));
        return totalDistance;
    }

    public void defineMapReference() {
        this.map = new MapReference();
        for (Vertex v : Set) {
            if (v.getX() < map.getMinX()) {
                map.setMinX(v.getX());
            }
            if (v.getX() > map.getMaxX()) {
                map.setMaxX(v.getX());
            }
            if (v.getY() < map.getMinY()) {
                map.setMinY(v.getY());
            }
            if (v.getY() > map.getMaxY()) {
                map.setMaxY(v.getY());
            }
            if (v.getZ() < map.getMinZ()) {
                map.setMinZ(v.getZ());
            }
            if (v.getZ() > map.getMaxZ()) {
                map.setMaxZ(v.getZ());
            }
        }
    }
}

