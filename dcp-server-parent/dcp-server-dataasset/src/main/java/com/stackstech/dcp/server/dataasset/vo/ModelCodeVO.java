package com.stackstech.dcp.server.dataasset.vo;

import com.stackstech.dcp.server.dataasset.model.ModelCode;

import java.util.List;

/**
 * 模型Code VO类
 */
public class ModelCodeVO extends ModelCode {

    private List<ModelCodeVO> modelCodes;

    public List<ModelCodeVO> getModelCodes() {
        return modelCodes;
    }

    public void setModelCodes(List<ModelCodeVO> modelCodes) {
        this.modelCodes = modelCodes;
    }
}
