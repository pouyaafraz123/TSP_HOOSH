package com.projects.hoosh_tsp_project.model;

public abstract class Controller<M extends Model, V extends View<M>> {
    protected M model;
    protected V view;

    protected Controller(M model, V view) {
        this.model = model;
        this.view = view;
    }
}