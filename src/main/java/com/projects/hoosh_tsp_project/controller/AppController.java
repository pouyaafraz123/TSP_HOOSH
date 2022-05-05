package com.projects.hoosh_tsp_project.controller;


import com.projects.hoosh_tsp_project.model.AppModel;
import com.projects.hoosh_tsp_project.model.Controller;
import com.projects.hoosh_tsp_project.setup.ServiceLocator;
import com.projects.hoosh_tsp_project.tsp.Path;
import com.projects.hoosh_tsp_project.tsp.Population;
import com.projects.hoosh_tsp_project.view.AppView;
import com.projects.hoosh_tsp_project.view.Chart;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppController extends Controller<AppModel, AppView> {
    private final DecimalFormat format = new DecimalFormat("#,##0.0000");
    ServiceLocator serviceLocator;
    boolean isStopped;
    boolean isStarted;
    Path bestPath;
    Thread GA;

    ArrayList<Pair<Long,Double>> datas = new ArrayList<>();
    AtomicLong dataCount = new AtomicLong(1);

    public AppController(AppModel model, AppView view) {
        super(model, view);
        serviceLocator = ServiceLocator.getServiceLocator();
        view.startBTN.setOnAction((ActionEvent) -> {
            if (!isStarted) {
                runEvaluation();
            }
            serviceLocator.getLogger().info("start evaluation");
        });

        view.stopBTN.setOnAction((ActionEvent) -> {
            {
                breakEvaluation();
            }
            serviceLocator.getLogger().info("stop evaluation");
            new Chart(datas);
        });

        view.selectFileBTN.setOnAction((ActionEvent) -> {
            if (!isStarted) {
                getImportFile();
            }
            serviceLocator.getLogger().info("file selected");
        });
        serviceLocator.getLogger().info("Application controller initialized");
    }

    private void runEvaluation() {
        boolean exit = false;
        isStarted = true;
        bestPath = null;
        File f = new File(view.fileDirTXF.getText());
        if (f.exists() && !f.isDirectory()) {
            if (model.correctStructure(f)) {
                model.createVertices(f);
            } else {
                exit = true;
            }
        } else {
            model.createRandomVertices(Integer.parseInt(view.verticesTXF.getText()));
        }
        model.vertexSet.defineMapReference();

        Platform.runLater(() -> {
            view.clear(view.painting);
            view.drawVertices(model.vertexSet, model.vertexSet.map);
        });

        model.cleanPopBook();
        if (!exit) {
            Task<Void> task1 = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    isStopped = false;
                    AtomicInteger popCount = new AtomicInteger(1);
                    while (!isStopped) {

                        String pop = "POPULATION " + popCount.getAndIncrement();
                        Platform.runLater(() -> {
                            view.pop.setText(pop);
                        });

                        int nSize = Integer.parseInt(view.populationSizeTXF.getText());
                        double nRate = Double.parseDouble(view.mutationRateTXF.getText());
                        model.population = new Population(model.vertexSet, nSize, nRate);

                        if (bestPath == null) {
                            bestPath = model.population.getRandomPath();
                        }

                        int i = Integer.parseInt(view.evolutionScopeTXF.getText());
                        AtomicInteger generationCount = new AtomicInteger(1);
                        while (i > 0 && !isStopped) {
                           // System.out.println("evolution: " + i);
                            String generation = "GENERATION " + generationCount.getAndIncrement();
                            Platform.runLater(() -> {
                                view.generation.setText(generation);
                            });
                            model.population.evolution();
                            Path p = model.population.getFittestPath();

                            if (model.vertexSet.getTotalDistance(p) < model.vertexSet.getTotalDistance(bestPath)) {
                                bestPath = p;
                                Double n = model.vertexSet.getTotalDistance(bestPath);

                                Platform.runLater(() -> {
                                    view.minDistanceTXF.setText("BEST DISTANCE : " + format.format(n));
                                    datas.add(new Pair<>(dataCount.getAndIncrement(),n));
                                   // System.out.println(n);
                                    view.bestPathArea.setText("BEST PATH : " + bestPath.getInfo(model.vertexSet));
                                });
                                Platform.runLater(() -> {
                                    view.clear(view.painting);
                                    view.drawPaths(model.vertexSet.map, bestPath);
                                    view.drawVertices(model.vertexSet, model.vertexSet.map);
                                });
                            }
                            i--;
                        }
                        //model.cleanPopBook();
                        String c = (model.population.getInfo());
                     //   System.out.println(c);
                        model.writeToPopBook("========================== "+pop+" ==========================\n");
                        model.writeToPopBook(c);
                     //   model.writeToPopBook(model.population.getDetailedInfo());
                        Thread.sleep(Long.parseLong(view.pauseTimeTXF.getText()));
                    }

                    isStarted = false;
                    return null;
                }

            };
            GA = new Thread(task1);
            GA.setDaemon(true);
            GA.start();
        }


    }

    private void breakEvaluation() {
        isStopped = true;
        System.out.println();
        System.out.println("Fittest path: " + bestPath.getInfo());
        System.out.println("============");
    }

    private void getImportFile() {
        File aReturn;
        FileChooser fc = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Text files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(filter);
        aReturn = fc.showOpenDialog(view.getStage());
        if (aReturn != null) {
            view.fileDirTXF.setText(aReturn.getAbsolutePath());
        }
    }
}
