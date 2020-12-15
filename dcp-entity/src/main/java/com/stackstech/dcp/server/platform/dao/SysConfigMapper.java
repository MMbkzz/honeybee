package com.stackstech.dcp.server.platform.dao;

import com.stackstech.dcp.server.platform.model.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Mapper
@Repository
public interface SysConfigMapper {

    List<SysConfig> queryAll(SysConfig sysConfig);

    SysConfig queryByPrimaryKey(@Param("id") Long id);

    int insert(SysConfig sysConfig);

    int update(SysConfig sysConfig);

    int delete(@Param("id") Long id);
}
