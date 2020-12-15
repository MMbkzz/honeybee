package com.stackstech.dcp.connector.core.entity;

import com.stackstech.dcp.connector.core.enums.MetaDataTypeEnum;
import lombok.Data;

@Data
public class DriverMetaData<T> {

    private MetaDataTypeEnum dataType;

    private T data;

    public DriverMetaData(MetaDataTypeEnum dataType, T data) {
        this.dataType = dataType;
        this.data = data;
    }

}
