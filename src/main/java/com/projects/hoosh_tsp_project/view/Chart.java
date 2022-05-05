package com.projects.hoosh_tsp_project.view;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.ChartZoomManager;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class Chart extends Stage {
    private final XYChart.Series<Number,Number> series = new XYChart.Series<>();
    private final ArrayList<Pair<Long,Double>> pairs = new ArrayList<>();
    public Chart(ArrayList<Pair<Long, Double>> datas){
        pairs.addAll(datas);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream("Data.txt"));
            writer.write(datas.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(createView());
        this.setScene(scene);
        this.show();
    }

    private ArrayList<Pair<Long, Double>> readData() throws IOException {
        ArrayList<Pair<Long,Double>> pairs = new ArrayList<>();
        Scanner scanner = new Scanner(Files.newInputStream(new File("PopBook/populations.txt").toPath()));
        while (scanner.hasNextLine()){
            pairs.add(new Pair<>(Long.parseLong(scanner.nextLine().replace("=","").split(" ")[2])
                    ,Double.parseDouble(scanner.nextLine().split(" : ")[1])));
        }


        return pairs;

    }

    public AnchorPane createView(){
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(600);
        pane.setPrefHeight(400);
        series.setName("DATA");
        for (Pair<Long,Double> pair:pairs){
            series.getData().add(new XYChart.Data<>(pair.getKey(),pair.getValue()));
        }
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("NO.");
        xAxis.setAutoRanging(true);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("BEST DISTANCE");
        LineChart<Number,Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(true);
        chart.setCreateSymbols(false);
        ChartZoomManager zoomManager = new ChartZoomManager(pane,new Rectangle(600,400),chart);
        zoomManager.setMouseWheelZoomAllowed(true);
        zoomManager.setMouseFilter(event -> {
            if (event.isDragDetect())event.consume();
        });
        zoomManager.setZoomAnimated(true);

        zoomManager.start();
        ChartPanManager panManager = new ChartPanManager(chart);
        panManager.start();
        chart.getStyleClass().add("chart-series-line");
        try {
            chart.getStylesheets().add(new File("Style.css").toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        chart.getData().add(series);
        AnchorPane.setLeftAnchor(chart,0.0);
        AnchorPane.setRightAnchor(chart,0.0);
        AnchorPane.setTopAnchor(chart,0.0);
        AnchorPane.setBottomAnchor(chart,0.0);
        pane.getChildren().add(chart);
        return pane;
    }
}
