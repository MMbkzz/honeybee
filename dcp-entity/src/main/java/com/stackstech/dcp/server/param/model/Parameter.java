package com.stackstech.dcp.server.param.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 参数表<driver / datasource>实体类
 * <p>
 * dcp_parameter
 */
@Data
public class Parameter {
    private Long id;                            //主键<自增>
    private String objectType;                  //参数实体类型
    private String objectId;                    //参数实体ID
    private Integer seqNum;                     //参数序列
    private String paramName;                   //参数名称
    private String paramValue;                  //参数值
    private String paramDesc;                   //参数描述
    private String displayName;                 //参数显示名称
    private String checkRegexp;                 //校验正则
    private String isRequired;                  //是否必须
    private Timestamp createTime;               //创建时间
    private Long createBy;                      //创建人
    private Timestamp updateTime;               //更新时间
    private Long updateBy;                      //更新人

}
