package com.stackstech.honeybee.server.quality.vo;

import com.stackstech.honeybee.server.core.annotation.AddGroup;
import com.stackstech.honeybee.server.core.annotation.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QualityRuleVo {

    @NotNull(message = "invalid rule id", groups = {UpdateGroup.class})
    private Long ruleId;

    @NotBlank(message = "quality rule name cannot be null", groups = {AddGroup.class})
    private String ruleName;

    @NotBlank(message = "quality rule type cannot be null", groups = {AddGroup.class})
    private String ruleType;

    @NotNull(message = "src assets model id cannot be null", groups = {AddGroup.class})
    private Long scrAssetsModelId;

    @NotNull(message = "target assets model id cannot be null", groups = {AddGroup.class})
    private Long targetAssetsModelId;

    @NotBlank(message = "quality rule expression type cannot be null", groups = {AddGroup.class})
    private String ruleExpressionType;

    @NotNull(message = "quality rule expression cannot be null", groups = {AddGroup.class})
    @Size(min = 1, max = 10, message = "invalid quality rule expression", groups = {AddGroup.class})
    private List<String> ruleExpression;

    private String ruleDesc;

    @NotNull(message = "invalid job id", groups = {UpdateGroup.class})
    private Long jobId;

    @NotBlank(message = "quality job name cannot be null", groups = {AddGroup.class})
    private String jobName;

    @NotBlank(message = "quality job expression cannot be null", groups = {AddGroup.class})
    private String jobExpression;

    @Min(value = 1L, message = "invalid job order level", groups = {AddGroup.class})
    private Integer jobOrder;

    private String jobDesc;
}
