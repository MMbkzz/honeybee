package com.stackstech.honeybee.server.datasource.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 数据源实体类
 */
@Data
public class ServiceSource {
    private String id;                              //主键<DTS+6位数字序列号>
    private String driverId;                        //驱动ID
    private String serviceSourceName;               //数据源名称
    private String serviceSourceDesc;               //数据源描述
    private String statusCode;                      //数据源状态<enabled/disabled/deleted>
    private String stageCode;                       //数据源阶段<unpublished待发布/publishing发布中/published已发布/waiting待下线/stopping下线中/offline已下线>
    private Integer maxConnections;                 //最大连接数
    private Integer queryTimeout;                   //查询超时时间
    private Integer connTimeout;                    //连接超时时间
    private Timestamp createTime;                   //创建时间
    private Long createBy;                          //创建人
    private Timestamp updateTime;                   //更新时间
    private Long updateBy;                          //更新人
    private String createUser;                      //创建人名称
    private String updateUser;                      //更新人名称
    private Integer heldNumber;                     //持有数量
    private Integer usedNumber;                     //已使用数量
    private Integer remainNumber;                   //剩余数量

}
