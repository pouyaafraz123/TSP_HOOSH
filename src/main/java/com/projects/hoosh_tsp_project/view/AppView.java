package com.projects.hoosh_tsp_project.view;


import com.projects.hoosh_tsp_project.controller.ViewController;
import com.projects.hoosh_tsp_project.model.AppModel;
import com.projects.hoosh_tsp_project.model.Painting;
import com.projects.hoosh_tsp_project.model.View;
import com.projects.hoosh_tsp_project.setup.ServiceLocator;
import com.projects.hoosh_tsp_project.tsp.MapReference;
import com.projects.hoosh_tsp_project.tsp.Path;
import com.projects.hoosh_tsp_project.tsp.Vertex;
import com.projects.hoosh_tsp_project.tsp.VertexSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class AppView extends View<AppModel> {
    private final double radius = 20.0;
    ServiceLocator serviceLocator;
    AnchorPane root;
    public TextField minDistanceTXF;
    public TextField verticesTXF;
    public TextField populationSizeTXF;
    public TextField evolutionScopeTXF;
    public TextField mutationRateTXF;
    public TextField fileDirTXF;
    public Button selectFileBTN;
    public TextField pauseTimeTXF;
    public Button startBTN;
    public Button stopBTN;
    public TextArea bestPathArea;

    public TextField generation;
    public Painting painting;
    ViewController control;

    public AppView(Stage stage, AppModel model) {
        super(stage, model);
        stage.setTitle("TSP");
        serviceLocator = ServiceLocator.getServiceLocator();
        serviceLocator.getLogger().info("APPLICATION VIEW INITIALIZED");
    }

    @Override
    protected Scene createGUI() {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(new File("src/main/resources/com/projects/hoosh_tsp_project/View.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            root = loader.load();
            control = loader.getController();
            minDistanceTXF = control.getDistance();
            verticesTXF = control.getVertex();
            fileDirTXF = control.getFile();
            bestPathArea = control.getArea();
            mutationRateTXF = control.getRate();
            pauseTimeTXF = control.getPause();
            populationSizeTXF = control.getPopulation();
            evolutionScopeTXF = control.getMax();
            selectFileBTN = control.getFileBTN();
            startBTN = control.getStart();
            stopBTN = control.getStop();
            generation = control.getGeneration();
            control.getContainer().getChildren().add(getPainting());

            root.setId("app");
            return new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stage getStage() {
        return this.stage;
    }

    public Painting getPainting() {
        if (painting == null) {
            double nWidth = root.getPrefWidth() - 300;
            double nHeight = root.getPrefHeight() - 50;
            painting = new Painting(nWidth, nHeight);
            clear(painting);
        }
        return painting;
    }

    public void clear(Painting p) {
        GraphicsContext gc = painting.getGraphicsContext2D();
        gc.clearRect(0, 0, p.getWidth(), p.getHeight());
        gc.setFill(Color.rgb(14, 22, 33));
        gc.fillRect(0, 0, p.getWidth(), p.getHeight());
    }

    public void drawVertices(VertexSet vertexSet, MapReference map) {
        double xRatio;
        double yRatio;
        GraphicsContext gc = painting.getGraphicsContext2D();
        for (int i = 0; i < vertexSet.getNumberOfVertices(); i++) {
            Vertex v = vertexSet.getVertex(i);
            xRatio = getRatioX(v, map);
            yRatio = getRatioY(v, map);
            gc.setFill(Color.WHITE);
            gc.fillOval((painting.getWidth() - radius) * xRatio, (painting.getHeight() - radius) * yRatio, radius, radius);
            gc.setFill(Color.rgb(255, 9, 235));
            gc.setFont(Font.font(12));
            gc.fillText(v.getName(), ((painting.getWidth() - radius) * xRatio) + 4, ((painting.getHeight() - radius) * yRatio) + radius / 2 + 6, 30);
        }
    }

    private double getRatioX(Vertex v, MapReference mp) {
        return (v.getX() - mp.getMinX()) / mp.getMapWidth();
    }

    private double getRatioY(Vertex v, MapReference mp) {
        return (v.getY() - mp.getMinY()) / mp.getMapHeight();
    }

    public void drawPaths(MapReference map, Path path) {
        int max = path.getLength() - 1;
        double fromX;
        double fromY;
        double toX;
        double toY;
        int from;
        int to;
        GraphicsContext gc = painting.getGraphicsContext2D();
        gc.setFill(Color.RED);
        for (int i = 0; i < max; i++) {
            from = path.get(i);
            to = path.get(i + 1);
            drawLine(map, from, to, gc);
        }
        from = path.get(max);
        to = path.get(0);
        drawLine(map,from,to,gc);
    }

    private void drawLine(MapReference map, int from, int to, GraphicsContext gc) {
        double fromX;
        double toY;
        double fromY;
        double toX;
        fromX = (painting.getWidth() - radius) * getRatioX(model.vertexSet.getVertex(from), map) + radius / 2;
        fromY = (painting.getHeight() - radius) * getRatioY(model.vertexSet.getVertex(from), map) + radius / 2;
        toX = (painting.getWidth() - radius) * getRatioX(model.vertexSet.getVertex(to), map) + radius / 2;
        toY = (painting.getHeight() - radius) * getRatioY(model.vertexSet.getVertex(to), map) + radius / 2;
        gc.setStroke(Color.AQUA);
        gc.setLineWidth(2);
        gc.strokeLine(fromX, fromY, toX, toY);
    }
}