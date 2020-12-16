package com.stackstech.honeybee.server.operation.service.impl;

import com.stackstech.honeybee.core.cache.ResourceActiveCache;
import com.stackstech.honeybee.core.cache.ResourceExpectCache;
import com.stackstech.honeybee.core.cache.ResourceHoldCache;
import com.stackstech.honeybee.core.util.CommonUtils;
import com.stackstech.honeybee.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import com.stackstech.honeybee.server.operation.service.SourceMonitorService;
import com.stackstech.honeybee.server.operation.vo.InstanceVO;
import com.stackstech.honeybee.server.operation.vo.SourceMonitorQueryVO;
import com.stackstech.honeybee.server.operation.vo.SourceMonitorVO;
import com.stackstech.honeybee.server.platform.dao.InstanceMapper;
import com.stackstech.honeybee.server.platform.dao.InstanceResourceMapper;
import com.stackstech.honeybee.server.platform.model.Instance;
import com.stackstech.honeybee.server.platform.model.InstanceResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源监控Service
 */
@Service
public class SourceMonitorServiceImpl implements SourceMonitorService {

    @Autowired
    private ResourceActiveCache resourceActiveCache;
    @Autowired
    private ResourceHoldCache resourceHoldCache;
    @Autowired
    private ResourceExpectCache resourceExpectCache;

    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private InstanceResourceMapper instanceResourceMapper;

    /**
     * 获取数据源监控列表
     *
     * @param queryVO
     * @return
     * @throws Exception
     */
    @Override
    public List<SourceMonitorVO> queryAll(SourceMonitorQueryVO queryVO) throws Exception {
        List<SourceMonitorVO> monitors = new ArrayList<>();
        //query ServiceSource
        List<ServiceSource> list = serviceSourceMapper.queryAll(CommonUtils.elementToMap(queryVO));
        if (list != null && list.size() > 0) {
            for (ServiceSource serviceSource : list) {
                List<Instance> instances = new ArrayList<>();
                Integer sourceUsedNum = 0;
                Integer sourceRemainNum = 0;
                Integer sourceHoldNum = 0;
                //query Instance
                List<InstanceResource> instanceResources = instanceResourceMapper.queryBySourceId(serviceSource.getId());
                if (instanceResources != null && instanceResources.size() > 0) {
                    for (InstanceResource instanceResource : instanceResources) {
                        Instance instance = instanceMapper.queryByPrimaryKey(instanceResource.getInstanceId());
                        if (instance != null) {
                            Integer usedNum = 0;
                            Integer remainNum = 0;
                            Integer holdNum = 0;
                            //获取源活跃数量和持有数量
                            Integer expectNum = resourceExpectCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                            Integer activeNum = resourceActiveCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                            Integer totalNum = resourceHoldCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                            if (totalNum != null) {
                                if (activeNum != null) {
                                    usedNum = activeNum;
                                }
                                if (totalNum >= usedNum) {
                                    remainNum = totalNum - usedNum;
                                }
                                holdNum = totalNum;
                            }
                            instance.setHeldNumber(holdNum > 0 ? holdNum : 0);
                            instance.setUsedNumber(usedNum > 0 ? usedNum : 0);
                            instance.setRemainNumber(remainNum > 0 ? remainNum : 0);
                            instance.setExpectNumber(expectNum > 0 ? expectNum : 0);
                            instances.add(instance);

                            sourceUsedNum += (usedNum != null ? usedNum : 0);
                            sourceRemainNum += (remainNum != null ? remainNum : 0);
                            sourceHoldNum += (holdNum != null ? holdNum : 0);
                        }
                    }
                }
                serviceSource.setHeldNumber(sourceHoldNum);
                serviceSource.setUsedNumber(sourceUsedNum);
                serviceSource.setRemainNumber(sourceRemainNum);
                SourceMonitorVO sourceMonitorVO = this.parseMonitor2VO(serviceSource, instances);
                monitors.add(sourceMonitorVO);
            }
        }
        return monitors;
    }

    /**
     * 获取数据源列表
     *
     * @param queryVO
     * @return
     * @throws Exception
     */
    @Override
    public int countAll(SourceMonitorQueryVO queryVO) throws Exception {
        return serviceSourceMapper.countAll(CommonUtils.elementToMap(queryVO));
    }

    /**
     * 获取实例监控列表
     *
     * @param queryVO
     * @return
     */
    @Override
    public List<InstanceVO> queryInstance(SourceMonitorQueryVO queryVO) {
        List<InstanceVO> monitors = new ArrayList<>();
        Instance ins = new Instance();
        ins.setName(queryVO.getInstanceName());
        ins.setStatusCode(queryVO.getStatusCode());

        List<Instance> instances = instanceMapper.queryAll(ins);
        if (instances != null && instances.size() > 0) {
            for (Instance instance : instances) {
                List<ServiceSource> serviceSources = new ArrayList<>();
                Integer instanceExpectNum = 0;
                Integer instanceUsedNum = 0;
                Integer instanceRemainNum = 0;
                Integer instanceHoldNum = 0;
                //query ServiceSource
                List<InstanceResource> instanceResources = instanceResourceMapper.queryByInstanceId(instance.getId());
                if (instanceResources == null) {
                    continue;
                }
                if (instanceResources != null && instanceResources.size() > 0) {
                    for (InstanceResource instanceResource : instanceResources) {
                        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(instanceResource.getServiceSourceId());
                        if (serviceSource != null && serviceSource.getMaxConnections() != null) {
                            Integer usedNum = 0;
                            Integer remainNum = 0;
                            Integer holdNum = 0;
                            //获取源活跃数量和持有数量
                            Integer expectNum = resourceExpectCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                            Integer activeNum = resourceActiveCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                            Integer totalNum = resourceHoldCache.get(instance.getHost() + ":" + instance.getPort(), serviceSource.getId());
                            if (totalNum != null) {
                                if (activeNum != null) {
                                    usedNum = activeNum;
                                }
                                if (totalNum >= usedNum) {
                                    remainNum = totalNum - usedNum;
                                }
                                holdNum = totalNum;
                            }
                            serviceSource.setHeldNumber(holdNum > 0 ? holdNum : 0);
                            serviceSource.setMaxConnections(holdNum > 0 ? holdNum : 0);
                            serviceSource.setUsedNumber(usedNum > 0 ? usedNum : 0);
                            serviceSource.setRemainNumber(remainNum > 0 ? remainNum : 0);
                            serviceSources.add(serviceSource);

                            instanceExpectNum += (expectNum != null ? expectNum : 0);
                            instanceUsedNum += (usedNum != null ? usedNum : 0);
                            instanceRemainNum += (remainNum != null ? remainNum : 0);
                            instanceHoldNum += (holdNum != null ? holdNum : 0);
                        }
                    }
                }
                instance.setExpectNumber(instanceExpectNum);
                instance.setHeldNumber(instanceHoldNum);
                instance.setUsedNumber(instanceUsedNum);
                instance.setRemainNumber(instanceRemainNum);
                InstanceVO instanceVO = this.parseInstance2VO(instance, serviceSources);
                monitors.add(instanceVO);
            }
        }
        return monitors;
    }

    /**
     * 获取实例监控计数
     *
     * @param queryVO
     * @return
     */
    @Override
    public int countInstance(SourceMonitorQueryVO queryVO) {
        Instance ins = new Instance();
        ins.setName(queryVO.getInstanceName());
        ins.setStatusCode(queryVO.getStatusCode());
        return instanceMapper.countAll(ins);
    }

    /**
     * 将数据源监控转换为VO
     *
     * @param serviceSource
     * @param instances
     * @return
     */
    private SourceMonitorVO parseMonitor2VO(ServiceSource serviceSource, List<Instance> instances) {
        if (serviceSource != null) {
            SourceMonitorVO sourceMonitorVO = new SourceMonitorVO();
            sourceMonitorVO.setId(serviceSource.getId());
            sourceMonitorVO.setServiceSourceName(serviceSource.getServiceSourceName());
            sourceMonitorVO.setStatusCode(serviceSource.getStatusCode());
            sourceMonitorVO.setMaxConnections(serviceSource.getMaxConnections());
            sourceMonitorVO.setHeldNumber(serviceSource.getHeldNumber());
            sourceMonitorVO.setUsedNumber(serviceSource.getUsedNumber());
            sourceMonitorVO.setRemainNumber(serviceSource.getRemainNumber());

            sourceMonitorVO.setInstances(instances);
            return sourceMonitorVO;
        }
        return null;
    }

    /**
     * 将INstance转换为VO
     *
     * @param instance
     * @param serviceSources
     * @return
     */
    private InstanceVO parseInstance2VO(Instance instance, List<ServiceSource> serviceSources) {
        if (instance != null) {
            InstanceVO instanceVO = new InstanceVO();
            instanceVO.setId(instance.getId());
            instanceVO.setHost(instance.getHost());
            instanceVO.setPort(instance.getPort());
            instanceVO.setName(instance.getName());
            instanceVO.setStatusCode(instance.getStatusCode());
            instanceVO.setStageCode(instance.getStageCode());
            instanceVO.setExpectNumber(instance.getExpectNumber());
            instanceVO.setHeldNumber(instance.getHeldNumber());
            instanceVO.setUsedNumber(instance.getUsedNumber());
            instanceVO.setRemainNumber(instance.getRemainNumber());

            instanceVO.setServiceSources(serviceSources);
            return instanceVO;
        }
        return null;
    }
}
