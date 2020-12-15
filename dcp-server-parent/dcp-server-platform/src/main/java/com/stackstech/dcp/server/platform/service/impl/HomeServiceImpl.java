package com.stackstech.dcp.server.platform.service.impl;

import com.stackstech.dcp.server.dataasset.dao.DataAssetAreaMapper;
import com.stackstech.dcp.server.dataasset.dao.DataAssetTopicMapper;
import com.stackstech.dcp.server.dataasset.dao.ModelCodeMapper;
import com.stackstech.dcp.server.dataasset.dao.ServiceModelMapper;
import com.stackstech.dcp.server.dataservice.dao.AppUserMapper;
import com.stackstech.dcp.server.dataservice.dao.DataServiceMapper;
import com.stackstech.dcp.server.dataservice.model.AppUser;
import com.stackstech.dcp.server.datasource.dao.ServiceSourceMapper;
import com.stackstech.dcp.server.datasource.model.ServiceSource;
import com.stackstech.dcp.server.operation.service.SourceMonitorService;
import com.stackstech.dcp.server.operation.vo.SourceMonitorQueryVO;
import com.stackstech.dcp.server.operation.vo.SourceMonitorVO;
import com.stackstech.dcp.server.platform.dao.InstanceMapper;
import com.stackstech.dcp.server.platform.model.Instance;
import com.stackstech.dcp.server.platform.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private DataServiceMapper dataServiceMapper;
    @Autowired
    private ServiceModelMapper serviceModelMapper;
    @Autowired
    private DataAssetAreaMapper assetAreaMapper;
    @Autowired
    private DataAssetTopicMapper assetTopicMapper;
    @Autowired
    private ModelCodeMapper modelCodeMapper;

    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private ServiceSourceMapper serviceSourceMapper;
    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private SourceMonitorService sourceMonitorService;


    /**
     * 指标概览
     *
     * @return
     */
    @Override
    public Map<String, Object> queryIndex() throws Exception {
        Map<String, Object> index = new HashMap<>();

        //计算资产服务指标
        this.assetIndex(index);

        //计算能力模型指标
        this.abilityIndex(index);

        //计算数据源指标
        this.sourceIndex(index);

        //计算连接数指标
        this.connectIndex(index);

        return index;
    }

    /**
     * 计算资产模型指标
     *
     * @param index
     */
    private void assetIndex(Map<String, Object> index) {
        int assetCount = 0;
        List<Map<String, Object>> topicList = dataServiceMapper.queryAssetTopicCount();
        if (topicList != null && topicList.size() > 0) {
            for (Map<String, Object> map : topicList) {
                Integer areaCount = map.get("areaCount") != null ? Integer.parseInt(map.get("areaCount").toString()) : 0;
                assetCount += areaCount;
            }
        }

        index.put("assetCount", assetCount);
        index.put("assetList", topicList);
    }


    /**
     * 计算能力模型指标
     *
     * @param index
     */
    private void abilityIndex(Map<String, Object> index) {
        int assetCount = 0;
        List<Map<String, Object>> topicList = dataServiceMapper.queryAbilityTopicCount();
        if (topicList != null && topicList.size() > 0) {
            for (Map<String, Object> map : topicList) {
                Integer areaCount = map.get("areaCount") != null ? Integer.parseInt(map.get("areaCount").toString()) : 0;
                assetCount += areaCount;
            }
        }

        index.put("abilityCount", assetCount);
        index.put("abilityList", topicList);
    }

    /**
     * 计算连接数量
     *
     * @param index
     */
    private void connectIndex(Map<String, Object> index) throws Exception {
        List<SourceMonitorVO> sources = sourceMonitorService.queryAll(new SourceMonitorQueryVO());
        int maxCount = 0;
        int heldCount = 0;
        int usedCount = 0;
        int remainCount = 0;

        if (sources != null && sources.size() > 0) {
            for (SourceMonitorVO sourceMonitorVO : sources) {
                Integer maxMum = sourceMonitorVO.getMaxConnections();
                Integer heldNum = sourceMonitorVO.getHeldNumber();
                Integer usedNum = sourceMonitorVO.getUsedNumber();
                Integer remainNum = sourceMonitorVO.getRemainNumber();

                maxCount += (maxMum != null ? maxMum : 0);
                heldCount += (heldNum != null ? heldNum : 0);
                usedCount += (usedNum != null ? usedNum : 0);
                remainCount += (remainNum != null ? remainNum : 0);
            }
        }
        index.put("maxCount", maxCount);
        index.put("heldCount", heldCount);
        index.put("usedCount", usedCount);
        index.put("remainCount", remainCount);
    }

    /**
     * 计算数据源&实例数
     *
     * @param index
     */
    private void sourceIndex(Map<String, Object> index) {
        //实例数量
        List<Instance> instances = instanceMapper.queryAll(new Instance());
        int insCount = (instances != null ? instances.size() : 0);

        //数据源数量
        List<ServiceSource> serviceSources = serviceSourceMapper.queryAll(new HashMap<String, Object>());
        int sourceCount = (serviceSources != null ? serviceSources.size() : 0);

        //服务用户数量
        List<AppUser> appUsers = appUserMapper.queryAll(new AppUser());
        int appCount = (appUsers != null ? appUsers.size() : 0);

        index.put("insCount", insCount);
        index.put("sourceCount", sourceCount);
        index.put("appCount", appCount);
    }

}
