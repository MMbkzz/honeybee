package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull(message = "request parameter cannot be null")
public class AssetsCatalogEntity {

    private Long id;

    private Integer isroot;

    private String catalogName;

    private String catalogCode;

    private Long catalogParentId;

    private String catalogType;

    private Integer catalogOrder;

    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long owner;

    @ApiModelProperty(hidden = true)
    private Date updatetime;

    @ApiModelProperty(hidden = true)
    private Date createtime;

    private String desc;

}