package com.stackstech.honeybee.apiserver.service;

import com.stackstech.honeybee.server.platform.model.Instance;

public interface LifeCycleService {

    void startup();

    Instance register();

    void shotdown();
}
