package com.stackstech.honeybee.server.dataasset.dao;

import com.stackstech.honeybee.server.dataasset.model.ServiceModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据资产模型表Dao
 * <p>
 * 表名 data_model
 */
@Mapper
@Repository
public interface ServiceModelMapper {

    /**
     * 新增数据资产模型
     *
     * @param serviceModel
     * @return
     */
    int insert(ServiceModel serviceModel);


    /**
     * 根据编号删除资产模型
     *
     * @param id
     * @return
     */
    int delete(@Param("id") String id);

    /**
     * 编辑资产模型
     *
     * @param serviceModel
     * @return
     */
    int update(ServiceModel serviceModel);

    /**
     * 资产模型查询列表
     *
     * @param map
     * @return
     */
    List<ServiceModel> queryAll(Map<String, Object> map);

    /**
     * 获取父模型
     *
     * @return
     */
    List<ServiceModel> queryAllParent(@Param("topicId") String topicId);

    /**
     * 资产模型详情查询
     *
     * @param id
     * @return
     */
    ServiceModel queryByPrimaryKey(@Param("id") String id);

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
     * 重命名校验
     *
     * @param modelName
     * @return
     */
    ServiceModel queryByName(@Param("modelName") String modelName);

    /**
     * 根据Topic Id获取模型
     *
     * @param topicId
     * @return
     */
    List<ServiceModel> queryByTopicId(@Param("topicId") String topicId);

    /**
     * 根据
     *
     * @param serviceSourceId
     * @return
     */
    List<ServiceModel> queryBySourceId(@Param("serviceSourceId") String serviceSourceId);

    /**
     * 查询资产模型
     *
     * @param map
     * @return
     */
    List<ServiceModel> queryAssetsModel(Map<String, Object> map);

    /**
     * 查询能力模型
     *
     * @param map
     * @return
     */
    List<ServiceModel> queryAbilityModel(Map<String, Object> map);

    /**
     * 资产模型计数
     *
     * @param map
     * @return
     */
    int countAssetsModel(Map<String, Object> map);

    /**
     * 能力模型计数
     *
     * @param map
     * @return
     */
    int countAbilityModel(Map<String, Object> map);

    /**
     * 全局搜索查询asset
     *
     * @param queryString
     * @param userId
     * @return
     */
    List<ServiceModel> queryAssetByCondition(@Param("queryString") String queryString, @Param("userId") String userId);

    /**
     * 全局搜索查询asset
     *
     * @param queryString
     * @param userId
     * @return
     */
    List<ServiceModel> queryAbilityByCondition(@Param("queryString") String queryString, @Param("userId") String userId);

    /**
     * 全局计数asset
     *
     * @param queryString
     * @param userId
     * @return
     */
    int countAssetByCondition(@Param("queryString") String queryString, @Param("userId") String userId);

    /**
     * 全局计数ability
     *
     * @param queryString
     * @param userId
     * @return
     */
    int countAbilityByCondition(@Param("queryString") String queryString, @Param("userId") String userId);
}
