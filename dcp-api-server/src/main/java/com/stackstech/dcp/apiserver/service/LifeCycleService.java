package com.stackstech.dcp.apiserver.service;

import com.stackstech.dcp.server.platform.model.Instance;

public interface LifeCycleService {

    void startup();

    Instance register();

    void shotdown();
}
