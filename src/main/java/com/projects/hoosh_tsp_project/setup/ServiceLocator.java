package com.projects.hoosh_tsp_project.setup;

import java.util.logging.Logger;

public class ServiceLocator {
    private static ServiceLocator serviceLocator;
    final private String PopBookDir = System.getProperty("user.dir") + "/PopBook/";
    final private String PopBookFile = PopBookDir + "populations.txt";
    private Logger logger;

    private ServiceLocator() {
    }

    public static ServiceLocator getServiceLocator() {
        if (serviceLocator == null)
            serviceLocator = new ServiceLocator();
        return serviceLocator;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getPopBookFile() {
        return PopBookFile;
    }

    public String getPopBookDir() {
        return PopBookDir;
    }
}
