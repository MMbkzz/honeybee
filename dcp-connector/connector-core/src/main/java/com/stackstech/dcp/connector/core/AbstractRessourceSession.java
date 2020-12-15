package com.stackstech.dcp.connector.core;

public abstract class AbstractRessourceSession {

    protected ClassLoader classLoader;

    protected int maxLimit;

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
