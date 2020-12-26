package com.stackstech.honeybee.server.quality.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LivyConf implements Serializable {

    private String file;

    private String className;

    private List<String> args;

    private String name;

    private String queue;

    private Long numExecutors;

    private Long executorCores;

    private String driverMemory;

    private String executorMemory;

    private Map<String, String> conf;

    private List<String> jars;

    private List<String> files;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Long getNumExecutors() {
        return numExecutors;
    }

    public void setNumExecutors(Long numExecutors) {
        this.numExecutors = numExecutors;
    }

    public Long getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(Long executorCores) {
        this.executorCores = executorCores;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public Map<String, String> getConf() {
        return conf;
    }

    public void setConf(Map<String, String> conf) {
        this.conf = conf;
    }

    public List<String> getJars() {
        return jars;
    }

    public void setJars(List<String> jars) {
        this.jars = jars;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

}
