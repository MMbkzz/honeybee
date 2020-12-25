package com.stackstech.honeybee.server.datasource.dao;

import com.stackstech.honeybee.server.datasource.model.ServiceSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据源DAO
 */
@Mapper
@Repository
public interface ServiceSourceMapper {

    /**
     * 根据条件获取datasource列表
     *
     * @param dataSource
     * @return
     */
    List<ServiceSource> query(ServiceSource dataSource);

    /**
     * 根据条件获取datasource列表
     *
     * @param map
     * @return
     */
    List<ServiceSource> queryAll(Map<String, Object> map);

    /**
     * 根据状态获取datasource列表
     *
     * @param status
     * @return
     */
    List<ServiceSource> queryByStatus(@Param("status") String[] status);

    /**
     * 根据条件获取datasource
     *
     * @param id
     * @return
     */
    ServiceSource queryByPrimaryKey(@Param("id") String id);

    /**
     * D
     * 根据名称获取datasource
     *
     * @param serviceSourceName
     * @return
     */
    ServiceSource queryByName(@Param("serviceSourceName") String serviceSourceName);

    /**
     * 获取自增主键
     *
     * @return
     */
    String queryPrimaryKey();

    /**
     * 新增数据源
     *
     * @param serviceSource
     * @return
     */
    int insert(ServiceSource serviceSource);

    /**
     * 更新数据源
     *
     * @param serviceSource
     * @return
     */
    int update(ServiceSource serviceSource);

    /**
     * 删除数据源
     *
     * @param id
     * @return
     */
    int delete(@Param("id") String id);

    /**
     * 获取记录数
     *
     * @param map
     * @return
     */
    int countAll(Map<String, Object> map);

    List<ServiceSource> queryByCondition(@Param("queryString") String queryString);

    int countByCondition(@Param("queryString") String queryString);
}
