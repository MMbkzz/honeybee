package com.stackstech.honeybee.server.quality.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NotNull(message = "quality rule parameter cannot be null")
public class QualityRuleVo {

    private Long ruleId;

    @NotNull(message = "quality rule name cannot be null")
    private String ruleName;

    @NotNull(message = "quality rule type cannot be null")
    private String ruleType;

    @Min(value = 1L, message = "invalid src assets model id")
    @NotNull(message = "src assets model id cannot be null")
    private Long scrAssetsModelId;

    @Min(value = 1L, message = "invalid target assets model id")
    @NotNull(message = "target assets model id cannot be null")
    private Long targetAssetsModelId;

    @NotNull(message = "quality rule expression type cannot be null")
    private String ruleExpressionType;

    @NotNull(message = "quality rule expression cannot be null")
    private List<String> ruleExpression;

    private String ruleDesc;

    private Long jobId;

    @NotNull(message = "quality job name cannot be null")
    private String jobName;

    @NotNull(message = "quality job expression cannot be null")
    private String jobExpression;

    @Min(value = 1L, message = "invalid job order level")
    private Integer jobOrder;

    private String jobDesc;
}
