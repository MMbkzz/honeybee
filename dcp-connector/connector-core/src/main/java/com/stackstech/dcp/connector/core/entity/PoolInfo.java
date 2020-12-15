package com.stackstech.dcp.connector.core.entity;

import lombok.Data;

@Data
public class PoolInfo {

    /**
     * 初始化最大连接总数
     */
    private int maxTotal;

    /**
     * 最大连接数量
     */
    private int maxIdle;


    /**
     * 最小连接数量
     */
    private int minIdle;
    private int numIdle;

    /**
     * 活跃连接数量
     */
    private int numActive;


}
