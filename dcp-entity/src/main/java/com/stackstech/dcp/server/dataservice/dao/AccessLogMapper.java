package com.stackstech.dcp.server.dataservice.dao;

import com.stackstech.dcp.server.dataservice.model.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * api服务 APP访问日志DAO
 */
@Mapper
@Repository
public interface AccessLogMapper {

    /**
     * 新增数据api访问日志表
     *
     * @param accessLog
     * @return
     */
    int insert(AccessLog accessLog);


    /**
     * 根据编号删除api访问日志表
     *
     * @param id
     * @return
     */
    int delete(@Param("id") Integer id);

    /**
     * 编辑api访问日志表
     *
     * @param accessLog
     * @return
     */
    int update(AccessLog accessLog);

    /**
     * api访问日志表查询列表
     *
     * @param map
     * @return
     */
    List<AccessLog> queryAll(Map<String, Object> map);

    /**
     * api访问日志表详情查询
     *
     * @param id
     * @return
     */
    AccessLog queryByPrimaryKey(@Param("id") Integer id);

    int countAll(Map<String, Object> map);

    List<AccessLog> query(Map<String, Object> map);

    /**
     * 获取服务访问数量排名
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryServiceCount(Map<String, Object> map);

    /**
     * 获取用户访问数量排名
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryAppCount(Map<String, Object> map);

    /**
     * 服务访问时长排名
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryServiceTime(Map<String, Object> map);

    /**
     * DB访问时长排名
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDBTime(Map<String, Object> map);

    /**
     * 服务平均访问时长排名
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryAvgService(Map<String, Object> map);

    /**
     * DB平均访问时长排名
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryAvgDB(Map<String, Object> map);

    int countByCondition(@Param("queryString") String queryString);

    List<AccessLog> queryByCondition(@Param("queryString") String queryString);
}
