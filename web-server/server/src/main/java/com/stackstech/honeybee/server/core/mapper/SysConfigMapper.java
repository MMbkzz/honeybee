package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysConfigMapper {

    int insertSelective(SysConfigEntity record);

    SysConfigEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysConfigEntity record);

    SysConfigEntity selectByConfigKey(String configKey);

    int updateByConfigKey(@Param("configKey") String configKey, @Param("configValue") String configValue);

}