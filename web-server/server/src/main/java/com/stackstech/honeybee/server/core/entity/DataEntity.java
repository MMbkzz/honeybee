package com.stackstech.honeybee.server.core.entity;

import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DataEntity {

    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long owner;

    @ApiModelProperty(hidden = true)
    private Date updatetime;

    @ApiModelProperty(hidden = true)
    private Date createtime;

    public void create(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
    }

    public void update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
    }

}
