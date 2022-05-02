package com.projects.hoosh_tsp_project.setup;

import com.projects.hoosh_tsp_project.model.Model;
import javafx.concurrent.Task;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupClass extends Model {

    ServiceLocator serviceLocator;
    public final Task<Void> initializer = new Task<Void>() {

        @Override
        protected Void call() {
            serviceLocator = ServiceLocator.getServiceLocator();
            createDir(serviceLocator.getPopBookDir());
            Logger logger = getCustomLogger();
            serviceLocator.setLogger(logger);
            return null;
        }
    };

    public SetupClass() {
        super();
    }

    private static Logger getCustomLogger() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINEST);
        Handler[] defaultHandlers = rootLogger.getHandlers();
        defaultHandlers[0].setLevel(Level.INFO);
        return rootLogger;
    }

    public void initialize() {
        new Thread(initializer).start();
    }

    public void createDir(String cPath) {
        File file = new File(cPath);
        boolean isDirectoryCreated = file.mkdir();
        if (isDirectoryCreated) {
            System.out.println("directory successfully created: " + cPath);
        } else {
            System.out.println("directory already existing: " + cPath);
            deleteDir(file);
            System.out.println("directory deleted: " + cPath);
            createDir(cPath);
        }
    }

    private void deleteDir(File dir) {
        File[] files = dir.listFiles();
        for (File myFile : files) {
            if (myFile.isDirectory()) {
                deleteDir(myFile);
            }
            myFile.delete();
        }
        dir.delete();
    }
}
