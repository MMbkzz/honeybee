package com.stackstech.honeybee.server.core.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NotNull(message = "query parameter cannot be null")
public class AssetsCatalogQuery extends PageQuery {

    @Min(value = 1L, message = "Invalid data id")
    private Long catalogParentId;

    private String catalogType;

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> map = super.getParameter();
        map.put("catalogParentId", catalogParentId);
        map.put("catalogType", catalogType);
        return map;
    }
}
