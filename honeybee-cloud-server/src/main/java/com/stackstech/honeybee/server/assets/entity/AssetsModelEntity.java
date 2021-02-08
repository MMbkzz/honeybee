package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stackstech.honeybee.common.entity.AbstractDataEntity;
import com.stackstech.honeybee.common.entity.JsonParameterList;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.assets.vo.AssetsModelVo;
import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsModelEntity extends AbstractDataEntity<AssetsModelEntity> {
    private Long id;

    private String assetsModelName;

    private String assetsModelCode;

    private Long assetsCatalogDomain;

    private Long assetsCatalogTopic;

    private Long datasourceId;

    private JsonParameterList datasourceMeta;

    private String expression;

    private String desc;

    private String assetsCatalogDomainName;

    private String assetsCatalogTopicName;

    @JsonIgnore
    private AssetsModelVo assetsModelVo;

    @Override
    public AssetsModelEntity build(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE;
        this.createtime = new Date();
        this.updatetime = new Date();
        this.assetsModelCode = CommonUtil.generateEntityCode();
        return this;
    }

    @Override
    public AssetsModelEntity update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return this;
    }

    @Override
    public AssetsModelEntity copy(Object vo) {
        CommonUtil.copyProperties(vo, this);
        this.assetsModelVo = (AssetsModelVo) vo;
        return this;
    }

}