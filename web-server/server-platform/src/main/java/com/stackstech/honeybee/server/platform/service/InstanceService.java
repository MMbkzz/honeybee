package com.stackstech.honeybee.server.platform.service;

import com.stackstech.honeybee.server.platform.model.Instance;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface InstanceService {
    List<Instance> queryAll(Instance instance) throws Exception;

    int countAll(Instance instance) throws Exception;

    ResponseEntity<?> insert(Instance instance, HttpServletRequest req) throws Exception;

    ResponseEntity<?> update(Instance instance, HttpServletRequest req) throws Exception;

    ResponseEntity<?> changeStatus(Instance instance, HttpServletRequest req) throws Exception;

    int delete(String id) throws Exception;


    /**
     * 获取等待上线实例
     * 实例信息
     * 状态：未知
     * 阶段：未激活
     *
     * @return
     */
    List<Instance> getWait4Online();

    /**
     * 获取等待下线实例
     * 实例信息
     * 状态：待停止
     *
     * @return
     */
    List<Instance> getWait4Offline();

    /**
     * 获取可被分配资源的实例
     * 实例信息
     * 状态：未知, 正常
     * 阶段：已激活，已初始化，已注册，已上线
     *
     * @return
     */
    List<Instance> getUsable();

    /**
     * @param host
     * @return
     */
    Instance getByHost(String host);

    /**
     * 通过数据源编号获取实例列表
     *
     * @param serviceSourceId
     * @return
     */
    List<Instance> getByServiceSourceId(String serviceSourceId);

    /**
     * @param instanceId
     * @param sourceId
     * @param expectNumber
     */
    void saveInstanceResource(String instanceId, String sourceId, Integer expectNumber);

}
