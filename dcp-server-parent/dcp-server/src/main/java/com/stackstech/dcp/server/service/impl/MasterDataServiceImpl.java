package com.stackstech.dcp.server.service.impl;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.cache.InstanceOfflineCache;
import com.stackstech.dcp.core.cache.ResourceExpectCache;
import com.stackstech.dcp.core.cache.ResourceExpectStatusCache;
import com.stackstech.dcp.core.enums.ResourceLoadStatusEnum;
import com.stackstech.dcp.core.enums.ServiceSourceStateEnum;
import com.stackstech.dcp.core.util.JacksonUtil;
import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.datasource.service.ServiceSourceService;
import com.stackstech.dcp.server.platform.model.Instance;
import com.stackstech.dcp.server.platform.service.InstanceService;
import com.stackstech.dcp.server.service.AuditLogService;
import com.stackstech.dcp.server.service.MasterDataService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MasterDataServiceImpl implements MasterDataService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private ServiceSourceService serviceSourceService;

    @Autowired
    private ResourceExpectCache resourceExpectCache;

    @Autowired
    private ResourceExpectStatusCache resourceExpectStatusCache;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private InstanceOfflineCache instanceOfflineCache;

    @Override
    public void execute() {

        refreshLoadStatus();

        // step1. 获取实例
        List<Instance> onLineInstance = instanceService.getWait4Online();
        List<Instance> ofLineInstance = instanceService.getWait4Offline();
        List<Instance> usableInstance = instanceService.getUsable();

        /*
         无可用、上（下）线实例， 结束操作
         注：有可用数据源时， 也结束操作。
         原因：初始化 或者 重发布 时， 没有实例可分配
         */
        if (CollectionUtils.isEmpty(usableInstance)
                && CollectionUtils.isEmpty(onLineInstance)
                && CollectionUtils.isEmpty(ofLineInstance)) {

            auditLogService.logMessage("没有实例");
            return;
        }

        // step2. 获取资源
        List<ServiceSource> onLineSource = serviceSourceService.getEnable();
        List<ServiceSource> oflineSource = serviceSourceService.getDisable();

        // step3. 负载均衡
        distribute(onLineInstance, ofLineInstance, usableInstance, onLineSource, oflineSource);

    }

    @Override
    public void distribute(List<Instance> onLineInstance,
                           List<Instance> ofLineInstance,
                           List<Instance> usableInstance,
                           List<ServiceSource> onLineSource,
                           List<ServiceSource> oflineSource) {
        // step1. 下线数据源
        if (CollectionUtils.isNotEmpty(oflineSource)) {
            offSource(oflineSource);
        }

        // step2. 下线实例
        if (CollectionUtils.isNotEmpty(ofLineInstance)) {
            offInstance(ofLineInstance);
        }

        //  step3. 重发布
        if (CollectionUtils.isNotEmpty(onLineInstance)
                || CollectionUtils.isNotEmpty(ofLineInstance)) {
            balance(onLineSource, usableInstance, "instance");
        } else {
            balance(onLineSource, usableInstance, "resource");
        }
    }

    private void refreshLoadStatus() {
        Set<String> keys = resourceExpectStatusCache.keys();
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        String[] up = {ResourceLoadStatusEnum.running.code, ResourceLoadStatusEnum.done.code};
        String[] down = {ResourceLoadStatusEnum.stopping.code, ResourceLoadStatusEnum.stopped.code};

        for (String key : keys) {
            Map<String, String> status = resourceExpectStatusCache.getNaturo(key);
            if (MapUtils.isEmpty(status)) {
                continue;
            }

            for (Map.Entry<String, String> entry : status.entrySet()) {
                // 发布阶段， 并且没有运行中的数据源， 阶段更新为已发布
                if (ArrayUtils.contains(up, entry.getValue())
                        && !status.containsValue(ResourceLoadStatusEnum.running.code)) {
                    resourceExpectStatusCache.deleteNaturo(key);

                    serviceSourceService.updateStage(key.substring(key.lastIndexOf(":") + 1), ServiceSourceStateEnum.published.code);
                }
                // 停止阶段， 并且没有停止中的数据源， 阶段更新为已停止
                else if (ArrayUtils.contains(down, entry.getValue())
                        && !status.containsValue(ResourceLoadStatusEnum.stopping.code)) {
                    resourceExpectStatusCache.deleteNaturo(key);

                    serviceSourceService.updateStage(key.substring(key.lastIndexOf(":") + 1), ServiceSourceStateEnum.offline.code);
                }

                break;
            }
        }

    }

    private void offSource(List<ServiceSource> oflineSource) {
        if (CollectionUtils.isEmpty(oflineSource)) {
            return;
        }

        Set<String> keys = resourceExpectCache.keys();
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        for (String key : keys) {
            // 设置数据源下线状态
            // 从实例资源表中过滤出关联的实例，并写入“停止中”状态
            // 更新预期资源数
            oflineSource.forEach(source -> {
                List<Instance> instances = instanceService.getByServiceSourceId(source.getId());
                if (CollectionUtils.isEmpty(instances)) {
                    return;
                }
                instances.forEach(instance -> {
                    String status = resourceExpectStatusCache.get(source.getId(), instance.getHost() + ":" + instance.getPort());
                    if (ResourceLoadStatusEnum.stopped.code.equals(status)
                            || ResourceLoadStatusEnum.stopping.code.equals(status)) {
                        return;
                    }
                    logger.warn("预期资源状态:" + source.getId() + " @ " + instance.getHost() + ":" + instance.getPort() + " @ " + ResourceLoadStatusEnum.stopping.code);
                    resourceExpectStatusCache.put(source.getId(), instance.getHost() + ":" + instance.getPort(), ResourceLoadStatusEnum.stopping.code);
                });
                auditLogService.logMessage("下线数据源" + source.getId());
                Integer pool = resourceExpectCache.getNaturo(key, source.getId());
                if (null != pool && 0 != pool) {
                    resourceExpectCache.putNaturo(key, source.getId(), 0);
                }
            });
        }


    }

    private void offInstance(List<Instance> ofLineInstance) {
        if (CollectionUtils.isEmpty(ofLineInstance)) {
            return;
        }

        // 添加到下线列表
        ofLineInstance.stream().map(instance -> instance.getHost() + ":" + instance.getPort()).forEach(host -> {
            auditLogService.logMessage("下线实例" + host);
            Map<String, Integer> expects = resourceExpectCache.get(host);
            if (MapUtils.isEmpty(expects)) {
                return;
            }
            for (Map.Entry<String, Integer> expect : expects.entrySet()) {
                if (0 == expect.getValue()) {
                    continue;
                }

                resourceExpectCache.put(host, expect.getKey(), 0);
            }
            instanceOfflineCache.add(null, host);
        });
    }


    private void balance(List<ServiceSource> serviceSources, List<Instance> instances, String mode) {
        if (CollectionUtils.isEmpty(serviceSources)
                || CollectionUtils.isEmpty(instances)) {
            return;
        }


        List<ServiceSource> sources = new ArrayList<>();
        Map<String, Map<String, Integer>> instantceResources = new HashMap<>();

        Map<String, Integer> resources = new HashMap<>();

        serviceSources.forEach(serviceSource -> {
            try {
                // 只调整（上、下线，链接数量调整）了数据源时， 只有 已上线 且 已负载均衡 不需要重新调整
                if ("resource".equals(mode)
                        && serviceSourceService.isOnline(serviceSource)
                        && serviceSourceService.isBalanced(serviceSource, instances)) {
                    return;
                }


                resources.put(serviceSource.getId(), 0);
                sources.add(serviceSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        for (Instance instance : instances) {
            String host = instance.getHost() + ":" + instance.getPort();
            Map<String, Integer> allExpect = resourceExpectCache.get(host);
            if (null == allExpect) {
                allExpect = new HashMap<>();
            }
            allExpect.putAll(allExpect);
            instantceResources.put(host, allExpect);
        }

        sources.forEach(source -> balanceResource(source, instantceResources));

    }

    private void store(Map<String, Map<String, Integer>> instantceResources) {
        if (MapUtils.isEmpty(instantceResources)) {
            return;
        }

        instantceResources.keySet().forEach(instance -> {
            Map<String, Integer> resources = instantceResources.get(instance);
            if (MapUtils.isEmpty(resources)) {
                return;
            }
            Instance inst = instanceService.getByHost(instance);
            if (null == inst) {
                return;
            }
            resources.forEach((key, value) -> {
                instanceService.saveInstanceResource(inst.getId(), key, value);
                String status = resourceExpectStatusCache.get(key, instance);
                if (ResourceLoadStatusEnum.done.code.equals(status)
                        || ResourceLoadStatusEnum.running.code.equals(status)) {
                    return;
                }
                if (value.equals(resourceExpectCache.get(instance, key))) {
                    return;
                }
                logger.warn("预期资源状态:" + key + " @ " + instance + " @ " + ResourceLoadStatusEnum.running.code);
                resourceExpectStatusCache.put(key, instance, ResourceLoadStatusEnum.running.code);
            });
            auditLogService.logMessage("分配预期资源数:" + instance + " - " + JacksonUtil.beanToJson(resources));
            resourceExpectCache.put(instance, resources);
        });
    }

    private void balanceResource(ServiceSource dataSource, Map<String, Map<String, Integer>> instantceResources) {
        if (null == dataSource || MapUtils.isEmpty(instantceResources)) {
            return;
        }

        // 资源编号
        final String resourceId = dataSource.getId();
        // 连接池大小
        Integer poolSize = dataSource.getMaxConnections();
        // 实例节点个数
        int instanceSize = instantceResources.size();

        // 计算 - 平均每台应用实例资源数量
        final int avgSize = poolSize / instanceSize;
        // 计算 - 剩余需分配资源数量
        int lastSize = poolSize % instanceSize;

        Map<String, Map<String, Integer>> newSources = new HashMap<>();

        instantceResources.forEach((key, value) -> {
            value.put(resourceId, avgSize);

            newSources.put(key, new HashMap<String, Integer>() {
                {
                    put(resourceId, avgSize);
                }
            });
        });


        Map<String, Integer> pool = new TreeMap(Maps.transformValues(instantceResources, pool1 -> {
            Collection<Integer> poolSize1 = pool1.values();
            int sum = 0;
            if (CollectionUtils.isEmpty(poolSize1)) {
                return sum;
            }

            for (Integer size : poolSize1) {
                sum += size;
            }
            return sum;
        }));

        List<Map.Entry<String, Integer>> list = new LinkedList<>(pool.entrySet());
        list.sort((e1, e2) -> (e1.getValue()).compareTo(e2.getValue()));

        for (int i = 0; i < lastSize; i++) {
            String instance = list.get(i).getKey();
            instantceResources.get(instance).put(resourceId, instantceResources.get(instance).get(resourceId) + 1);
            newSources.get(instance).put(resourceId, newSources.get(instance).get(resourceId) + 1);
        }

        store(newSources);
    }


}
