package com.stackstech.dcp.server.dataasset.service.impl;

import com.stackstech.dcp.server.dataasset.dao.DataAssetAreaMapper;
import com.stackstech.dcp.server.dataasset.dao.DataAssetTopicMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelMapper;
import com.stackstech.dcp.server.dataasset.model.DataAssetArea;
import com.stackstech.dcp.server.dataasset.model.DataAssetTopic;
import com.stackstech.dcp.server.dataasset.model.ServiceModel;
import com.stackstech.dcp.server.dataasset.service.AssetMapService;
import com.stackstech.dcp.server.dataservice.dao.AccessLogMapper;
import com.stackstech.dcp.server.dataservice.dao.DataServiceMapper;
import com.stackstech.dcp.server.dataservice.model.DataService;
import com.stackstech.dcp.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.operations.dao.ServiceMonitorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Service
public class AssetMapServiceImpl implements AssetMapService {

    @Autowired
    private DataAssetAreaMapper assetAreaMapper;
    @Autowired
    private DataAssetTopicMapper assetTopicMapper;
    @Autowired
    private ServiceModelMapper serviceModelMapper;
    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private AccessLogMapper accessLogMapper;
    @Autowired
    private DataServiceMapper dataServiceMapper;
    @Autowired
    private ServiceMonitorMapper serviceMonitorMapper;

    /**
     * 查询资产地图
     *
     * @return
     */
    @Override
    public Map<String, Object> queryAll() {
        AtomicInteger id = new AtomicInteger(0);
        Map<String, Object> nodes = new HashMap<>();
        List<Map<String, Object>> nodeList = new ArrayList<>();
        List<Map<String, Object>> sideList = new ArrayList<>();

        //获取资产-主题-模型
        Map<String, Object> parentNode = new HashMap<>();
        parentNode.put("category", 0);
        parentNode.put("name", "华润置地");
        parentNode.put("parentId", 0);
        parentNode.put("id", 0);
        nodeList.add(parentNode);
        //领域处理
        List<DataAssetArea> areas = assetAreaMapper.queryAll(new DataAssetArea());
        if (areas != null && areas.size() > 0) {
            for (DataAssetArea assetArea : areas) {
                Map<String, Object> areaNode = this.areaHandler(id, assetArea, parentNode, nodeList, sideList);
                List<DataAssetTopic> topics = assetTopicMapper.queryByAreaId(assetArea.getId());
                if (topics != null && topics.size() > 0) {
                    //主题处理
                    for (DataAssetTopic assetTopic : topics) {
                        //查询父模型
                        List<ServiceModel> serviceModels = serviceModelMapper.queryAllParent(assetTopic.getId());
                        Map<String, Object> topicNode = this.topicHandler(id, assetTopic, areaNode, serviceModels, nodeList, sideList);
                        //模型处理
                        if (serviceModels != null && serviceModels.size() > 0) {
                            for (ServiceModel serviceModel : serviceModels) {
                                //查询子模型
                                List<ServiceModel> childModels = null;
                                if (serviceModel.getParentId() == null) {
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("parentId", serviceModel.getId());
                                    childModels = serviceModelMapper.queryAll(params);
                                }

                                Map<String, Object> parentModelNode = this.parentModelHandler(id, topicNode, serviceModel, childModels, nodeList, sideList);

                                if (childModels != null && childModels.size() > 0) {
                                    for (ServiceModel childModel : childModels) {
                                        this.modelHandler(id, parentModelNode, childModel, nodeList, sideList);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        nodes.put("nodes", nodeList);
        nodes.put("sides", sideList);
        return nodes;
    }


    /**
     * 主题处理
     *
     * @return
     */
    private Map<String, Object> topicHandler(AtomicInteger id, DataAssetTopic assetTopic, Map<String, Object> areaNode, List<ServiceModel> serviceModels, List<Map<String, Object>> nodeList, List<Map<String, Object>> sideList) {
        Map<String, Object> topicNode = new HashMap<>();
        topicNode.put("category", 2);
        topicNode.put("name", assetTopic.getTopicName());
        topicNode.put("parentId", areaNode.get("id"));
        topicNode.put("id", id.incrementAndGet());
        topicNode.put("value", (serviceModels != null ? serviceModels.size() : 0));

        Map<String, Object> topicSide = new HashMap<>();
        topicSide.put("source", areaNode.get("id"));
        topicSide.put("target", topicNode.get("id"));

        nodeList.add(topicNode);
        sideList.add(topicSide);

        return topicNode;
    }

    /**
     * 领域处理
     */
    private Map<String, Object> areaHandler(AtomicInteger id, DataAssetArea assetArea, Map<String, Object> parentNode, List<Map<String, Object>> nodeList, List<Map<String, Object>> sideList) {
        Map<String, Object> areaNode = new HashMap<>();
        areaNode.put("category", 1);
        areaNode.put("name", assetArea.getAreaName());
        areaNode.put("id", id.incrementAndGet());
        areaNode.put("parentId", parentNode.get("id"));
        areaNode.put("symbol", assetArea.getPicPath());

        Map<String, Object> areaSide = new HashMap<>();
        areaSide.put("source", parentNode.get("id"));
        areaSide.put("target", areaNode.get("id"));
        nodeList.add(areaNode);
        sideList.add(areaSide);

        return areaNode;
    }

    /**
     * 父模型处理
     */
    private Map<String, Object> parentModelHandler(AtomicInteger id, Map<String, Object> topicNode, ServiceModel serviceModel, List<ServiceModel> childModels, List<Map<String, Object>> nodeList, List<Map<String, Object>> sideList) {
        int invokeCount = 0;
        double responseAvg = 0;
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(serviceModel.getServiceSourceId());
        List<DataService> dataServices = dataServiceMapper.queryByModelId(serviceModel.getId());
        if (dataServices != null && dataServices.size() > 0) {
            DataService dataService = dataServices.get(0);
            Map<String, BigDecimal> accessMap = serviceMonitorMapper.queryAccessTime(dataService.getId());
            if (accessMap != null) {
                invokeCount = (accessMap.get("accessNum") != null) ? accessMap.get("accessNum").intValue() : 0;
                responseAvg = (accessMap.get("accessTime") != null) ? accessMap.get("accessTime").doubleValue() : 0;
            }
        }

        Map<String, Object> modelNode = new HashMap<>();
        modelNode.put("category", 3);
        modelNode.put("name", serviceModel.getModelName());
        modelNode.put("parentId", topicNode.get("id"));
        modelNode.put("id", id.incrementAndGet());
        modelNode.put("value", (childModels != null ? childModels.size() : 0));
        modelNode.put("sourceName", (serviceSource != null ? serviceSource.getServiceSourceName() : ""));
        modelNode.put("statusCode", serviceSource.getStatusCode());
        modelNode.put("invokeCount", invokeCount);
        modelNode.put("responseAvg", responseAvg);

        Map<String, Object> modelSide = new HashMap<>();
        modelSide.put("source", topicNode.get("id"));
        modelSide.put("target", modelNode.get("id"));

        nodeList.add(modelNode);
        sideList.add(modelSide);

        return modelNode;
    }

    /**
     * 模型处理
     */
    private void modelHandler(AtomicInteger id, Map<String, Object> parentModelNode, ServiceModel serviceModel, List<Map<String, Object>> nodeList, List<Map<String, Object>> sideList) {
        int invokeCount = 0;
        double responseAvg = 0;
        ServiceSource serviceSource = serviceSourceMapper.queryByPrimaryKey(serviceModel.getServiceSourceId());
        List<DataService> services = dataServiceMapper.queryByModelId(serviceModel.getId());
        if (services != null && services.size() > 0) {
            DataService dataService = services.get(0);
            Map<String, BigDecimal> accessMap = serviceMonitorMapper.queryAccessTime(dataService.getId());
            if (accessMap != null) {
                invokeCount = (accessMap.get("accessNum") != null) ? accessMap.get("accessNum").intValue() : 0;
                responseAvg = (accessMap.get("accessTime") != null) ? accessMap.get("accessTime").doubleValue() : 0;
            }
        }

        Map<String, Object> modelNode = new HashMap<>();
        modelNode.put("category", 4);
        modelNode.put("name", serviceModel.getModelName());
        modelNode.put("parentId", parentModelNode.get("id"));
        modelNode.put("id", id.incrementAndGet());
        modelNode.put("sourceName", (serviceSource != null ? serviceSource.getServiceSourceName() : ""));
        modelNode.put("statusCode", serviceSource.getStatusCode());
        modelNode.put("invokeCount", invokeCount);
        modelNode.put("responseAvg", responseAvg);

        Map<String, Object> modelSide = new HashMap<>();
        modelSide.put("source", parentModelNode.get("id"));
        modelSide.put("target", modelNode.get("id"));

        nodeList.add(modelNode);
        sideList.add(modelSide);
    }

}
