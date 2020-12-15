package com.stackstech.dcp.server.dataasset.service;

import com.stackstech.dcp.server.dataasset.model.ModelCode;

import java.util.List;

public interface ModelCodeService {
    List<ModelCode> queryAll(String parentCode);

    ModelCode query(Long id);

    List<ModelCode> queryStatus(String type);
}
