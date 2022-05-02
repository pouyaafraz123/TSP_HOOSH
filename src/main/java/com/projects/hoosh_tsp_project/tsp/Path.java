package com.projects.hoosh_tsp_project.tsp;

public class Path implements Cloneable {
    private static long count;
    private final long id;
    private final int[] order;
    private double fitness;

    public Path(int verticesCount, boolean shuffle) {
        this.id = ++count;
        this.order = new int[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            this.order[i] = i;
        }
        if (shuffle) {
            this.shuffle(verticesCount);
        }
    }

    private Path(Path copyOf) {
        this.order = copyOf.order.clone();
        this.id = copyOf.id;
    }

    public Path(int[] order) {
        this.id = ++count;
        this.order = order.clone();
    }

    public static boolean contains(final int[] array, final int v) {
        boolean result = false;
        for (int i : array) {
            if (i == v) {
                result = true;
                break;
            }
        }
        return result;
    }

    public Path clone() {
        return new Path(this);
    }

    private void shuffle(int intensity) {
        int max = this.getLength();
        int index1;
        int index2 = -1;
        boolean exit = max <= 1;
        if (max == 2) {
            this.swap(0, 1);
            exit = true;
        }
        if (!exit) {
            for (int i = 0; i <= intensity; i++) {
                index1 = index2;
                while (index1 == index2) {
                    index1 = (int) (Math.floor(max * Math.random()));
                    index2 = (int) (Math.floor(max * Math.random()));
                }
                this.swap(index1, index2);
            }
        }
    }

    private void swap(int a, int b) {
        int temp = this.order[a];
        this.order[a] = this.order[b];
        this.order[b] = temp;
    }

    public void mutate(double mutationRate) {
        int intensity;
        int length = this.getLength();
        double a = Math.floor(length * Math.abs(mutationRate));
        intensity = (int) (a);
        intensity = intensity / 2;
        this.shuffle(intensity);
    }

    public int getLength() {
        return this.order.length;
    }

    public int get(int i) {
        return this.order[i];
    }

    public String getInfo() {
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < this.getLength(); i++) {
            info.append("[").append(this.get(i)).append("] ");
        }
        info = new StringBuilder(info.substring(0, info.length() - 1));
        info.append("  {").append(this.fitness).append("}");
        return info.toString();
    }

    public String getInfo(VertexSet vs) {
        int index;
        Vertex v;
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < this.getLength(); i++) {
            index = this.get(i);
            v = vs.getVertex(index);
            info.append("[").append(v.getName()).append("] -> ");
        }
        info = new StringBuilder(info.substring(0, info.length() - 4));
        info.append("  {").append(this.fitness).append("}");
        info.append("  {").append(String.format("%.4f", vs.getTotalDistance(this))).append("}");

        return info.toString();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
