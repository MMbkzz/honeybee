package com.stackstech.dcp.server.dataservice.dao;

import com.stackstech.dcp.server.dataservice.model.DataService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * api数据服务
 */
@Mapper
@Repository
public interface DataServiceMapper {

    /**
     * 新增数据APP数据服务
     *
     * @param dataService
     * @return
     */
    int insert(DataService dataService);


    /**
     * 根据编号删除APP数据服务
     *
     * @param id
     * @return
     */
    int delete(@Param("id") String id);

    /**
     * 编辑APP数据服务
     *
     * @param dataService
     * @return
     */
    int update(DataService dataService);

    /**
     * APP数据服务查询列表
     *
     * @param map
     * @return
     */
    List<DataService> queryAll(Map<String, Object> map);

    List<DataService> query(List<String> ids);

    /**
     * APP数据服务详情查询
     *
     * @param id
     * @return
     */
    DataService queryByPrimaryKey(@Param("id") String id);


    /**
     * 根据modelId获取数据服务
     *
     * @param serviceModelId
     * @return
     */
    List<DataService> queryByModelId(@Param("serviceModelId") String serviceModelId);

    /**
     * APP数据服务详情查询
     *
     * @param dataServiceName
     * @return
     */
    DataService queryByName(@Param("dataServiceName") String dataServiceName);

    /**
     * 获取自增主键
     *
     * @return
     */
    String queryPrimaryKey();


    /**
     * 获取服务
     *
     * @param map
     * @return
     */

    List<DataService> queryAllByQuery(Map<String, Object> map);

    /**
     * 获取资产主题数量
     *
     * @return
     */
    List<Map<String, Object>> queryAssetTopicCount();

    /**
     * 获取能力主题数量
     *
     * @return
     */
    List<Map<String, Object>> queryAbilityTopicCount();

}
