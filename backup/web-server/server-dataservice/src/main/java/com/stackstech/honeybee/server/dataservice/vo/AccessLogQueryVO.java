package com.stackstech.honeybee.server.dataservice.vo;

import lombok.Data;

@Data
public class AccessLogQueryVO {

    private String appId;                       //APP ID
    private String dataServiceId;               //服务ID
    private String accessTimes;                  //访问耗时
    private String execTimes;                 //执行耗时
    private String dataServiceName;             //服务名称
    private String name;                        //访问用户
    private String startTime;                   //创建开始时间
    private String endTime;                     //创建结束时间
    private String message;                     //日志信息
    private String queryString;
    private String queryType;
    private Integer pageNo;
    private Integer pageSize;

}
