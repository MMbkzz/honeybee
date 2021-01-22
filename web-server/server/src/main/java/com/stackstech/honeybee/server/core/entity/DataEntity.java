package com.stackstech.honeybee.server.core.entity;

import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import lombok.Data;

import java.util.Date;

@Data
public class DataEntity<T> {

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    public T create(Long ownerId) {
        this.owner = ownerId;
        this.status = EntityStatusType.ENABLE.getStatus();
        this.createtime = new Date();
        this.updatetime = new Date();
        return (T) this;
    }

    public T update(Long ownerId) {
        this.owner = ownerId;
        this.updatetime = new Date();
        return (T) this;
    }

}
