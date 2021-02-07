package com.stackstech.honeybee.server.core.enums.types;

import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;
import lombok.Data;

@Data
public class EnumTypeMapping {

    private String name;
    private String code;

    public EnumTypeMapping build(BaseEnumTypeService enumType) {
        this.name = enumType.getName();
        this.code = enumType.getCode();
        return this;
    }

}
