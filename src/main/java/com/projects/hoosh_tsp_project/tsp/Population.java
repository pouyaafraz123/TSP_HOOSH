package com.projects.hoosh_tsp_project.tsp;

import java.util.ArrayList;

public class Population {
    private final VertexSet vertexSet;
    private final double mutationRate;
    private long generation;
    private ArrayList<Path> paths;

    public Population(VertexSet vertexSet, int size, double mutationRate) {
        this.generation = 0;
        this.vertexSet = vertexSet;
        this.mutationRate = mutationRate;

        this.paths = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Path p = new Path(vertexSet.getNumberOfVertices(), true);
            this.paths.add(p);
        }

        this.computeFitness();
    }

    private static Path pickRank(ArrayList<Path> list, int index) {
        Path path;
        ArrayList<Path> clonedList = new ArrayList<>(list);
        clonedList.sort((p1, p2) -> Double.compare(p2.getFitness(), p1.getFitness()));
        path = clonedList.get(index);

        return path;
    }

    private static Path cross(Path p1, Path p2) {
        Path crossOveredPath;
        int length = p1.getLength();
        int sequenceLength = (int) (length * Math.random());
        if (sequenceLength == 0) {
            sequenceLength = 2;
        }
        if (sequenceLength == length) {
            sequenceLength -= 2;
        }
        int startIndex = (int) ((length - sequenceLength) * Math.random());
        int[] order = new int[length];
        for (int i = 0; i < length; i++) {
            order[i] = -1;
        }
        for (int i = startIndex; sequenceLength > 0; i++) {
            order[i] = p1.get(i);
            sequenceLength--;
        }
        int n = 0;
        for (int i = 0; i < length; i++) {
            if (order[i] == -1) {
                boolean bool = false;
                while (!bool) {
                    if (Path.contains(order, p2.get(n))) {
                        n++;
                    } else {
                        bool = true;
                    }
                }
                order[i] = p2.get(n);
            }
        }
        crossOveredPath = new Path(order);
        return crossOveredPath;
    }

    public Path getRandomPath() {
        int index = (int) (this.paths.size() * Math.random());
        Path path = this.paths.get(index);
        return path.clone();
    }

    public Path getFittestPath() {
        Path fittestPath;
        this.computeFitness();
        fittestPath = this.paths.get(0);
        for (Path p : this.paths) {
            if (fittestPath.getFitness() < p.getFitness()) {
                fittestPath = p;
            }
        }
        return fittestPath.clone();
    }

    private void computeFitness() {
        double fitnessRef = this.getPointOfReference();
        double totalDistance;
        double newFitness;

        for (int i = 0; i < this.paths.size(); i++) {
            Path p = this.paths.get(i);
            totalDistance = vertexSet.getTotalDistance(p);
            newFitness = (fitnessRef - totalDistance) / fitnessRef;
            newFitness = newFitness / (double) (this.paths.size() - 1);
            p.setFitness(newFitness);
        }
    }

    private double getPointOfReference() {
        double point = 0;
        double distance;

        for (Path p : this.paths) {
            distance = vertexSet.getTotalDistance(p);
            point += distance;
        }

        return point;
    }

    public void evolution() {
        ArrayList<Path> newSociety = new ArrayList<>();
        this.computeFitness();
        Path p1 = pickRank(this.paths, 0);
        Path p2 = pickRank(this.paths, 1);
        while (newSociety.size() < this.paths.size()) {
            Path p3 = cross(p1, p2);
            p3.mutate(this.mutationRate);
            newSociety.add(p3);
        }

        this.paths = newSociety;
        this.generation++;
    }

    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        for (Path p : this.paths) {
            info.append(p.getInfo(this.vertexSet)).append("\n\n");
        }
        return info.toString();
    }

    public String getInfo() {
        String info = "BEST DISTANCE IN POPULATION : ";
        double currentLowestDistance = this.vertexSet.getTotalDistance(this.getFittestPath());
        info = info + String.format("%.4f \n", currentLowestDistance);
        return info;
    }
}
