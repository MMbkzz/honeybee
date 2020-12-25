package com.stackstech.honeybee.server.service;


import com.stackstech.honeybee.server.platform.model.Instance;

public interface HealthCheckService {

    void check();

    void check(Instance instance);

}
