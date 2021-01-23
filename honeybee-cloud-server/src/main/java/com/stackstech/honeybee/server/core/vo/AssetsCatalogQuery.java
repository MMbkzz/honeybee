package com.stackstech.honeybee.server.core.vo;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@ApiModel
@Data
@NotNull(message = "query parameter cannot be null")
public class AssetsCatalogQuery {

    @Min(value = 1L, message = "Invalid data id")
    private Long catalogParentId;

    private String catalogType;

    public Map<String, Object> getParameter() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("catalogParentId", catalogParentId);
        map.put("catalogType", catalogType);
        return map;
    }
}
