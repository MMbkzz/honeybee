package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.api.vo.DataServiceVo;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEntity extends AbstractDataEntity<DataServiceEntity, DataServiceVo> {

    private Long id;

    private String dataServiceName;

    private String dataServiceCode;

    private Long assetsModelId;

    private String datasourceMeta;

    private String serviceMeta;

    private Integer cacheExpire;

    private String expression;

    private String desc;

    @Override
    public DataServiceEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        this.dataServiceCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public DataServiceEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public DataServiceEntity build(Long ownerId, DataServiceVo vo) {
        DataServiceEntity entity = build(ownerId);
        BeanUtils.copyProperties(vo, entity);
        //TODO
        entity.setDatasourceMeta("TODO");
        entity.setServiceMeta(CommonUtil.toJsonString(vo.getDataServiceParameters()));
        entity.setExpression("TODO");
        return entity;
    }

    @Override
    public DataServiceEntity update(Long ownerId, DataServiceVo vo) {
        DataServiceEntity entity = update(ownerId);
        BeanUtils.copyProperties(vo, entity);
        //TODO
        entity.setDatasourceMeta("TODO");
        entity.setServiceMeta(CommonUtil.toJsonString(vo.getDataServiceParameters()));
        entity.setExpression("TODO");
        return entity;
    }
}