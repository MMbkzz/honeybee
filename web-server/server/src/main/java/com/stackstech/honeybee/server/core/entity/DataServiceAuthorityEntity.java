package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataServiceAuthorityEntity extends DataEntity {
    private Long id;

    private Long tenantId;

    private Long dataServiceId;

    private String authorityToken;

    private Long authorityExpire;

    private String authorityData;

    private String desc;

}