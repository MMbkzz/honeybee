package com.stackstech.dcp.server.dataasset.vo;

import com.stackstech.dcp.server.dataasset.model.DataAssetArea;
import com.stackstech.dcp.server.dataasset.model.DataAssetTopic;

/**
 * 资产主题VO
 */
public class DataAssetTopicVO extends DataAssetTopic {

    //资产领域名称
    private DataAssetArea dataAssetArea;

    public DataAssetArea getDataAssetArea() {
        return dataAssetArea;
    }

    public void setDataAssetArea(DataAssetArea dataAssetArea) {
        this.dataAssetArea = dataAssetArea;
    }
}
