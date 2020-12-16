package com.stackstech.honeybee.server.dataasset.service;

import com.stackstech.honeybee.server.dataasset.model.ModelCode;

import java.util.List;

public interface ModelCodeService {
    List<ModelCode> queryAll(String parentCode);

    ModelCode query(Long id);

    List<ModelCode> queryStatus(String type);
}
