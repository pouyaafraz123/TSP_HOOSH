package com.projects.hoosh_tsp_project.controller;


import com.projects.hoosh_tsp_project.model.AppModel;
import com.projects.hoosh_tsp_project.model.Controller;
import com.projects.hoosh_tsp_project.setup.ServiceLocator;
import com.projects.hoosh_tsp_project.tsp.Path;
import com.projects.hoosh_tsp_project.tsp.Population;
import com.projects.hoosh_tsp_project.view.AppView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class AppController extends Controller<AppModel, AppView> {
    private final DecimalFormat format = new DecimalFormat("#,##0.0000");
    ServiceLocator serviceLocator;
    boolean isStopped;
    boolean isStarted;
    Path bestPath;
    Thread GA;

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
                    AtomicInteger generationCount = new AtomicInteger(1);
                    while (!isStopped) {
                   /*     System.out.println("================================== Generation : "
                                + generationCount++ + " ==================================");*/
                        Platform.runLater(() -> view.generation.setText("GENERATION "+ generationCount.getAndIncrement()));
                        int nSize = Integer.parseInt(view.populationSizeTXF.getText());
                        double nRate = Double.parseDouble(view.mutationRateTXF.getText());
                        model.population = new Population(model.vertexSet, nSize, nRate);

                        if (bestPath == null) {
                            bestPath = model.population.getRandomPath();
                        }

                        int i = Integer.parseInt(view.evolutionScopeTXF.getText());
                        while (i > 0 && !isStopped) {
                           // System.out.println("evolution: " + i);
                            model.population.evolution();
                            Path p = model.population.getFittestPath();
                            if (model.vertexSet.getTotalDistance(p) < model.vertexSet.getTotalDistance(bestPath)) {
                                bestPath = p;
                                Double n = model.vertexSet.getTotalDistance(bestPath);

                                Platform.runLater(() -> {
                                    view.minDistanceTXF.setText("BEST DISTANCE : " + format.format(n));
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
                        String c = (model.population.getInfo());
                     //   System.out.println(c);

                        model.writeToPopBook(c + "\n");
                        model.writeToPopBook(model.population.getDetailedInfo());

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
