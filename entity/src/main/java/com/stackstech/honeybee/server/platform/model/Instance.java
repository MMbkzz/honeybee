package com.stackstech.honeybee.server.platform.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Instance {

    /**
     * 主键，实例编号，格式：INS+6位数字序列号，如：INS000001
     */
    private String id;
    /**
     * 实例名称
     */
    private String name;
    /**
     * 实例主机IP
     */
    private String host;
    /**
     * 主机用户
     */
    private String hostUser;
    /**
     * 主机密码
     */
    private String hostPassword;
    /**
     * 端口
     */
    private String port;
    /**
     * 存放路径
     */
    private String instancePath;
    /**
     * 状态 (unknown 未知 normal 正常 stopping 待停止 stopped 停止)
     */
    private String statusCode = "unknown";
    /**
     * 阶段 (1.unactivated 未激活、2.activated 已激活、3.initialized 已初始化、4.registered 已注册、5.online 已上线、6.unregister 取消注册、7.dealloc  释放资源、8.stopped 停止)
     */
    private String stageCode = "unknown";
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

    /**
     * 预期资源量
     */
    private Integer expectNumber;

    /**
     * 已持有资源数
     */
    private Integer heldNumber;

    /**
     * 已使用数量
     */
    private Integer usedNumber;

    /**
     * 剩余数量
     */
    private Integer remainNumber;

    private String createUser;

    private String updateUser;

    private String queryString;                 //全局搜索查询
    private String queryType;                   //查询类型public

    public Instance() {
    }


}
