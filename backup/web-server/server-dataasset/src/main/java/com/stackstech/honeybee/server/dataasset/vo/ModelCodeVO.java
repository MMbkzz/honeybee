package com.stackstech.honeybee.server.dataasset.vo;

import com.stackstech.honeybee.server.dataasset.model.ModelCode;

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
