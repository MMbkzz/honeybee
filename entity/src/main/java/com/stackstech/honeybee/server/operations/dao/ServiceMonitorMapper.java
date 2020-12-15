package com.stackstech.honeybee.server.operations.dao;

import com.stackstech.honeybee.server.operations.model.AppAccessKpi;
import com.stackstech.honeybee.server.operations.model.ServiceAccessKpi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 服务监控Mapper
 */
@Mapper
@Repository
public interface ServiceMonitorMapper {

    List<AppAccessKpi> queryAppDay(@Param("startTime") String startTime);

    List<AppAccessKpi> queryAppHour(@Param("startTime") String startTime);

    List<ServiceAccessKpi> queryServiceDay(@Param("startTime") String startTime);

    List<ServiceAccessKpi> queryServiceHour(@Param("startTime") String startTime);

    List<Map<String, Object>> queryServiceCountRank(Map<String, Object> map);

    List<Map<String, Object>> queryAppCountRank(Map<String, Object> map);

    List<Map<String, Object>> queryAccessTimeRank(Map<String, Object> map);

    List<Map<String, Object>> queryAccessAvgRank(Map<String, Object> map);

    List<Map<String, Object>> queryExecuteTimeRank(Map<String, Object> map);

    List<Map<String, Object>> queryExecuteAvgRank(Map<String, Object> map);


    /**
     * 资产地图访问
     *
     * @param dataServiceId
     * @return
     */
    Map<String, BigDecimal> queryAccessTime(@Param("dataServiceId") String dataServiceId);

    /**
     * 服务访问指标
     *
     * @param map
     * @return
     */
    Map<String, Object> queryAccessKpi(Map<String, Object> map);

    /**
     * 用户访问指标
     *
     * @param map
     * @return
     */
    int queryAppKpi(Map<String, Object> map);

    /**
     * 小时曲线图
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryAccessApiByHour(Map<String, Object> map);

    /**
     * 日曲线图
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryAccessApiByDay(Map<String, Object> map);
}
