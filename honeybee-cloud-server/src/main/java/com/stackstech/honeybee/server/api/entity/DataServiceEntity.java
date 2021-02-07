package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceEntity extends AbstractDataEntity<DataServiceEntity> {

    private Long id;

    private String dataServiceName;

    private String dataServiceCode;

    private Long assetsModelId;

    private JsonParameterMap serviceMeta;

    private Integer cacheExpire;

    private String expression;

    private String desc;

    private String assetsModelName;

    private JsonParameterMap datasourceMeta;

    private Long assetsCatalogDomain;

    private String assetsCatalogDomainName;

    private Long assetsCatalogTopic;

    private String assetsCatalogTopicName;

    @Override
    public DataServiceEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
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
    public DataServiceEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        return this;
    }

}