package com.stackstech.honeybee.server.api.vo;

import com.stackstech.honeybee.common.entity.DataAuthorityMeta;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NotNull(message = "data authority parameter cannot be null")
public class UpdateDataAuthorityMetaVo {

    @Min(value = 1L, message = "invalid data tenant id")
    private Long id;

    @NotNull(message = "data authority meta cannot be null")
    private List<DataAuthorityMeta> dataAuthorityMeta;


}
