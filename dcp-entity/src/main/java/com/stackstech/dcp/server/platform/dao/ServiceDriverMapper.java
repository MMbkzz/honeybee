package com.stackstech.dcp.server.platform.dao;

import com.stackstech.dcp.server.platform.model.ServiceDriver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 驱动关联Mapper
 */
@Mapper
@Repository
public interface ServiceDriverMapper {

    /**
     * 查询驱动列表
     *
     * @param serviceDriver
     * @return
     */
    List<ServiceDriver> queryAll(ServiceDriver serviceDriver);

    /**
     * 查询驱动
     *
     * @param id
     * @return
     */
    ServiceDriver queryByPrimaryKey(@Param("id") String id);

    /**
     * 根据名称获取驱动
     *
     * @param driverName
     * @return
     */
    ServiceDriver queryByName(@Param("driverName") String driverName);

    /**
     * 获取最大ID
     *
     * @return
     */
    String queryPrimaryKey();

    /**
     * 新增驱动
     *
     * @param serviceDriver
     * @return
     */
    int insert(ServiceDriver serviceDriver);

    /**
     * 更新驱动
     *
     * @param serviceDriver
     * @return
     */
    int update(ServiceDriver serviceDriver);


    /**
     * 删除驱动
     *
     * @param ids
     * @return
     */
    int delete(List<String> ids);

    /**
     * 获取记录数
     *
     * @param serviceDriver
     * @return
     */
    int countAll(ServiceDriver serviceDriver);

    int countByCondition(@Param("queryString") String queryString);

    List<ServiceDriver> queryByCondition(@Param("queryString") String queryString);
}
