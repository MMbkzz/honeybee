package com.stackstech.honeybee.server.dataasset.dao;

import com.stackstech.honeybee.server.dataasset.model.DataAssetTopic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据资产主题表Dao
 * <p>
 * 表名 dcp_data_model_topic
 */
@Mapper
@Repository
public interface DataAssetTopicMapper {

    /**
     * 新增数据资产主题
     *
     * @param dataAssetTopic
     * @return
     */
    int insert(DataAssetTopic dataAssetTopic);


    /**
     * 根据编号删除资产主题
     *
     * @param ids
     * @return
     */
    int delete(List<String> ids);

    /**
     * 编辑资产主题
     *
     * @param dataAssetTopic
     * @return
     */
    int update(DataAssetTopic dataAssetTopic);

    /**
     * 资产主题查询列表
     *
     * @param map
     * @return
     */
    List<DataAssetTopic> queryAll(Map<String, Object> map);

    /**
     * 资产主题详情查询
     *
     * @param id
     * @return
     */
    DataAssetTopic queryByPrimaryKey(@Param("id") String id);


    /**
     * 根据名称获取详情
     *
     * @param topicName
     * @return
     */
    DataAssetTopic queryByName(@Param("topicName") String topicName);

    /**
     * 获取自增主键
     *
     * @return
     */
    String queryPrimaryKey();

    /**
     * 获取记录数
     *
     * @param map
     * @return
     */
    int countAll(Map<String, Object> map);

    /**
     * 根据AreaId获取Topic
     *
     * @param areaId
     * @return
     */
    List<DataAssetTopic> queryByAreaId(@Param("areaId") String areaId);

    List<DataAssetTopic> queryByCondition(@Param("queryString") String queryString, @Param("userId") String userId);

    int countByCondition(@Param("queryString") String queryString, @Param("userId") String userId);

}
