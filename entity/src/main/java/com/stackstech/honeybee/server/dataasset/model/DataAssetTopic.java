package com.stackstech.honeybee.server.dataasset.model;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class DataAssetTopic {

    /**
     * 数据模型主题ID
     */
    private String id;

    /**
     * 关联资产领域主键ID
     */
    private String areaId;

    /**
     * 数据资产主题名称
     */
    private String topicName;

    /**
     * 数据资产主题描述
     */
    private String topicDesc;

    /**
     * 状态: enabled 启用 deleted 删除
     */
    private String statusCode;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 修改人
     */
    private Long updateBy;

    private Long owner;
    private String createUser;
    private String updateUser;

}
