package com.stackstech.honeybee.server.platform.dao;

import com.stackstech.honeybee.server.platform.model.InstanceResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Mapper
@Repository
public interface InstanceResourceMapper {

    InstanceResource queryByPrimaryKey(@Param("id") Long id);

    List<InstanceResource> queryBySourceId(@Param("serviceSourceId") String serviceSourceId);

    List<InstanceResource> queryByInstanceId(@Param("instanceId") String instanceId);

    List<InstanceResource> queryServiceSourceId();

    void delete(@Param("instanceId") String instanceId,
                @Param("serviceSourceId") String serviceSourceId);

    void insert(InstanceResource instanceResource);

    void deleteByInstanceId(@Param("instanceId") String instanceId);
}
