package com.stackstech.honeybee.server.dataasset.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 模型代码实体类
 */
@Data
public class ModelCode {
    private Long id;                                        //主键
    private String type;                                    //类型
    private String code;                                    //快码
    private String displayName;                             //显示名称
    private String parentType;                              //父类型
    private String parentCode;                              //父类型快码
    private String codeDesc;                                //快码描述
    private Long createBy;                                  //创建人
    private Timestamp createTime;                           //创建时间
    private Long updateBy;                                  //更新人
    private Timestamp updateTime;                           //更新时间

}
