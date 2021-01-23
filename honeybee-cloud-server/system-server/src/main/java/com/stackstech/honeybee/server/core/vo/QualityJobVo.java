package com.stackstech.honeybee.server.core.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "quality job parameter cannot be null")
public class QualityJobVo {

    private Long id;

    @NotNull(message = "quality job name cannot be null")
    private String jobName;

    @NotNull(message = "quality job expression cannot be null")
    private String jobExpression;

    @Min(value = 1L, message = "invalid job order level")
    private Integer jobOrder;

    private Integer status;

    private String desc;
}
