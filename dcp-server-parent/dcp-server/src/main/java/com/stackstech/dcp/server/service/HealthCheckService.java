package com.stackstech.dcp.server.service;


import com.stackstech.dcp.server.platform.model.Instance;

public interface HealthCheckService {

    void check();

    void check(Instance instance);

}
