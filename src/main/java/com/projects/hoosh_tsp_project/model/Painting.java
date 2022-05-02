package com.projects.hoosh_tsp_project.model;

import javafx.scene.canvas.Canvas;

public class Painting extends Canvas {
    private static long count;
    public long id;
    public double width;
    public double height;

    public Painting(double width, double height) {
        super(width, height);
        this.id = ++count;
        this.width = getWidth();
        this.height = getHeight();
        this.setStyle("-fx-background-color: rgb(14, 22, 33);-fx-padding: 10px");
        this.applyCss();

    }
}