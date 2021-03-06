package com.stackstech.honeybee.server.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.api.vo.DataServiceVo;
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

    private JsonParameterList serviceMeta;

    private Integer cacheExpire;

    @JsonIgnore
    private String expression;

    private String desc;

    private String assetsModelName;

    private JsonParameterList datasourceMeta;

    private Long assetsCatalogDomain;

    private String assetsCatalogDomainName;

    private Long assetsCatalogTopic;

    private String assetsCatalogTopicName;

    @JsonIgnore
    private DataServiceVo dataServiceVo;

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
        this.dataServiceVo = (DataServiceVo) vo;
        return this;
    }

}