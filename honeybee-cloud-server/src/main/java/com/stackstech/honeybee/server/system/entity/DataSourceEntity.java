package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import com.stackstech.honeybee.server.system.vo.DataSourceVo;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceEntity extends AbstractDataEntity<DataSourceEntity> {
    private Long id;

    private String datasourceName;

    private String datasourceCode;

    private DataSourceType datasourceType;

    private JsonParameterList datasourceConfig;

    private String desc;

    @JsonIgnore
    private DataSourceVo dataSourceVo;

    @Override
    public DataSourceEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
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
    public DataSourceEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        this.dataSourceVo = (DataSourceVo) vo;
        return this;
    }

}