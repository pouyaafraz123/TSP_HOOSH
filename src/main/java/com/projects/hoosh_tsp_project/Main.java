package com.projects.hoosh_tsp_project;

import com.projects.hoosh_tsp_project.controller.AppController;
import com.projects.hoosh_tsp_project.model.AppModel;
import com.projects.hoosh_tsp_project.setup.ServiceLocator;
import com.projects.hoosh_tsp_project.setup.SetupClass;
import com.projects.hoosh_tsp_project.view.AppView;
import com.projects.hoosh_tsp_project.view.Chart;
import javafx.concurrent.Worker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


public class Main extends Application {
    private static Main mainProgram;
    private AppView view;
    private ServiceLocator serviceLocator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        if (mainProgram == null) {
            mainProgram = this;
        } else {
            Platform.exit();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        SetupClass setupClass = new SetupClass();
        setupClass.initializer.stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED)
                        startApp();
                });
        setupClass.initialize();
    }

    public void startApp() {
        Stage appStage = new Stage();
        AppModel model = new AppModel();
        view = new AppView(appStage, model);
        new AppController(model, view);
        serviceLocator = ServiceLocator.getServiceLocator();
        view.start(true);

    }

    @Override
    public void stop() {
        if (view != null) {
            view.stop();
        }
        serviceLocator.getLogger().info("APPLICATION TERMINATED");
    }
}
