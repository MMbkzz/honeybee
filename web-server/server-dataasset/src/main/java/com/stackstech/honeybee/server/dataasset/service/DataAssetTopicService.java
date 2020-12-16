package com.stackstech.honeybee.server.dataasset.service;

import com.stackstech.honeybee.server.dataasset.model.DataAssetTopic;
import com.stackstech.honeybee.server.dataasset.vo.DataAssetTopicQueryVO;
import com.stackstech.honeybee.server.dataasset.vo.DataAssetTopicVO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据资产主题
 */
public interface DataAssetTopicService {

    /**
     * 资产主题详情查询
     *
     * @param queryVO
     * @return
     */
    List<Map<String, Object>> queryAll(DataAssetTopicQueryVO queryVO) throws Exception;

    /**
     * 资产主题详情查询
     *
     * @param id
     * @return
     */
    DataAssetTopicVO query(String id) throws Exception;

    /**
     * 新增数据资产主题
     *
     * @param dataAssetTopic
     * @return
     */
    int add(DataAssetTopic dataAssetTopic, Long userId) throws Exception;

    /**
     * 资产主题删除
     *
     * @param ids
     * @return
     */
    ResponseEntity<?> delete(List<String> ids) throws Exception;

    /**
     * 资产主题修改
     *
     * @param dataAssetTopic
     * @return
     */
    int update(DataAssetTopic dataAssetTopic, Long userId) throws Exception;


    int countAll(DataAssetTopicQueryVO queryVO) throws Exception;

    /**
     * 校验重名
     *
     * @param topicName
     * @return
     */
    DataAssetTopic queryByName(String topicName);
}
