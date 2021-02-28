package com.stackstech.honeybee.server.system.vo;

import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysConfigVo {

    @NotBlank(message = "config cannot be null", groups = {UpdateGroup.class})
    private String config;

}
