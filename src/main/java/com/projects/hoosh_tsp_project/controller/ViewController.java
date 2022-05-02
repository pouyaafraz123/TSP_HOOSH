package com.projects.hoosh_tsp_project.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ViewController {

    @FXML
    private TextField distance;

    @FXML
    private JFXTextField vertex;

    @FXML
    private JFXTextField population;

    @FXML
    private JFXTextField max;

    @FXML
    private JFXTextField rate;

    @FXML
    private JFXTextField file;

    @FXML
    private Button fileBTN;

    @FXML
    private JFXTextField pause;

    @FXML
    private Button start;

    @FXML
    private Button stop;

    @FXML
    private TextArea area;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField generation;

    public TextField getGeneration() {
        return generation;
    }

    public TextField getDistance() {
        return distance;
    }

    public JFXTextField getVertex() {
        return vertex;
    }

    public JFXTextField getPopulation() {
        return population;
    }

    public JFXTextField getMax() {
        return max;
    }

    public JFXTextField getRate() {
        return rate;
    }

    public JFXTextField getFile() {
        return file;
    }

    public Button getFileBTN() {
        return fileBTN;
    }

    public JFXTextField getPause() {
        return pause;
    }

    public Button getStart() {
        return start;
    }

    public Button getStop() {
        return stop;
    }

    public TextArea getArea() {
        return area;
    }

    public AnchorPane getContainer() {
        return container;
    }
}
