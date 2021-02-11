package com.stackstech.honeybee.server.api.vo;

import com.stackstech.honeybee.common.entity.DataAuthorityMeta;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateDataAuthorityMetaVo {

    @Min(value = 1L, message = "invalid data tenant id")
    private Long id;

    @NotNull(message = "data authority meta cannot be null")
    @Size(min = 1, max = 10000, message = "invalid data authority meta size")
    private List<DataAuthorityMeta> dataAuthorityMeta;


}
