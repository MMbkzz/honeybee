package com.stackstech.dcp.connector.core;

public abstract class AbstractResourceObserver {

    protected ResourceSessionFactory resourceSessionFactory;

    public abstract void add(String resourceId, ResourceSessionFactory sessionFactory);

    public abstract void update(String resourceId, ResourceSessionFactory sessionFactory);

    public abstract void close(String resourceId);

}
