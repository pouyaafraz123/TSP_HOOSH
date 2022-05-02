package com.projects.hoosh_tsp_project.model;

import com.projects.hoosh_tsp_project.setup.ServiceLocator;
import com.projects.hoosh_tsp_project.tsp.Population;
import com.projects.hoosh_tsp_project.tsp.Vertex;
import com.projects.hoosh_tsp_project.tsp.VertexSet;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AppModel extends Model {
    public VertexSet vertexSet;
    public Population population;
    ServiceLocator serviceLocator;

    public AppModel() {
        serviceLocator = ServiceLocator.getServiceLocator();
        serviceLocator.getLogger().info("APPLICATION MODEL INITIALIZED");
    }

    public void createRandomVertices(int n) {
        vertexSet = new VertexSet();
        for (int i = 0; i < n; i++) {
            Vertex v = Vertex.getRandom(0, 100, 0, 100, 0, 0);
            vertexSet.addVertex(v);
        }
        Vertex.count = 1;
    }

    public void createVertices(File file) {
        vertexSet = new VertexSet();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] aValues = line.split(",");
                Vertex v = new Vertex(aValues[0], Double.parseDouble(aValues[1]), Double.parseDouble(aValues[2]));
                if (aValues.length == 4) {
                    v.setZ(Double.parseDouble(aValues[3]));
                }
                vertexSet.addVertex(v);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToPopBook(String text) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serviceLocator.getPopBookFile(), true));
            bufferedWriter.write(text);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanPopBook() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serviceLocator.getPopBookFile(), false));
            bufferedWriter.write("");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean correctStructure(File file) {
        boolean isCorrect;
        String line;
        String name;
        String x;
        String y;
        String z = "0.0";
        boolean isFinished = false;

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            isFinished = true;
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("File cannot be open");
            alert.showAndWait();
            e.printStackTrace();
        }


        if (!isFinished) {
            InputStreamReader rd = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(rd, 1000 * 8192);
        }

        if (!isFinished) {
            try {
                while ((line = bufferedReader.readLine()) != null && !isFinished) {
                    String[] aValues = line.split(",");
                    name = aValues[0];
                    x = aValues[1];
                    y = aValues[2];
                    if (aValues.length == 4) {
                        z = aValues[3];
                    }
                    if (name.length() == 0) {
                        isFinished = true;
                    }
                    try {
                        Double.parseDouble(x);
                        Double.parseDouble(y);
                        Double.parseDouble(z);
                    } catch (NumberFormatException e) {
                        isFinished = true;
                    }
                    System.out.println(name + "|" + x + "|" + y + "|" + z);
                }
            } catch (IOException e) {
                isFinished = true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("File has not appropriate structure");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isCorrect = !isFinished;
        return isCorrect;
    }
}
