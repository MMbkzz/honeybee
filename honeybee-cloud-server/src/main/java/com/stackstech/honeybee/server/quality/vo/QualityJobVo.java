package com.stackstech.honeybee.server.quality.vo;

import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QualityJobVo {

    @NotNull(message = "invalid job id", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "quality job name cannot be null", groups = {AddGroup.class})
    private String jobName;

    @NotBlank(message = "quality job expression cannot be null", groups = {AddGroup.class})
    private String jobExpression;

    @Min(value = 1L, message = "invalid job order level", groups = {AddGroup.class})
    private Integer jobOrder;

    private Integer status;

    private String desc;
}
