package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.system.vo.DataSourceVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceEntity extends AbstractDataEntity<DataSourceEntity, DataSourceVo> {
    private Long id;

    private String datasourceName;

    private String datasourceCode;

    private String datasourceType;

    private String datasourceConfig;

    private String desc;

    @Override
    public DataSourceEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.datasourceCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public DataSourceEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataSourceEntity build(Long ownerId, DataSourceVo vo) {
        DataSourceEntity entity = build(ownerId);
        BeanUtils.copyProperties(vo, entity);
        entity.setDatasourceConfig(CommonUtil.toJsonString(vo.getDatasourceParameters()));
        return entity;
    }

    @Override
    public DataSourceEntity update(Long ownerId, DataSourceVo vo) {
        DataSourceEntity entity = update(ownerId);
        BeanUtils.copyProperties(vo, entity);
        entity.setDatasourceConfig(CommonUtil.toJsonString(vo.getDatasourceParameters()));
        return entity;
    }
}