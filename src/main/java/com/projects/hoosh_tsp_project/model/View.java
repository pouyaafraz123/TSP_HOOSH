package com.projects.hoosh_tsp_project.model;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public abstract class View<M extends Model> {
    protected Stage stage;
    protected Scene scene;
    protected M model;

    protected View(Stage stage, M model) {
        this.stage = stage;
        this.model = model;
        scene = createGUI();
        stage.setScene(scene);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11)
                stage.setFullScreen(true);
        });
    }

    protected abstract Scene createGUI();

    public void start(boolean resizeable) {
        stage.setResizable(resizeable);
        stage.show();
    }

    public void stop() {
        stage.hide();
    }

    public Stage getStage() {
        return stage;
    }
}
