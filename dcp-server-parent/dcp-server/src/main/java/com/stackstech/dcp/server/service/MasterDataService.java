package com.stackstech.dcp.server.service;


import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.platform.model.Instance;

import java.util.List;

public interface MasterDataService {

    void execute();

    /**
     * 负载均衡
     *
     * @param onLineInstance 已上线实例
     * @param ofLineInstance 已下线实例
     * @param usableInstance 可用实例
     * @param onLineSource   上线资源
     * @param oflineSource   下线资源
     */
    void distribute(List<Instance> onLineInstance,
                    List<Instance> ofLineInstance,
                    List<Instance> usableInstance,
                    List<ServiceSource> onLineSource,
                    List<ServiceSource> oflineSource);


}
