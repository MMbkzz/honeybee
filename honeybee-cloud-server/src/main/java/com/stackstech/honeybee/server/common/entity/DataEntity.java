package com.stackstech.honeybee.server.common.entity;

import lombok.Data;

import java.util.Date;

@Data
public abstract class DataEntity<T> {

    protected Integer status;

    protected Long owner;

    protected Date updatetime;

    protected Date createtime;

    abstract public T build(Long ownerId);

    abstract public T update(Long ownerId);

}
