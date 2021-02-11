package com.stackstech.honeybee.server.assets.vo;

import com.stackstech.honeybee.common.vo.PageQuery;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class AssetsModelQuery extends PageQuery {

    @Min(value = 1L, message = "Invalid domain id")
    private Long assetsCatalogDomain;

    @Min(value = 1L, message = "Invalid topic id")
    private Long assetsCatalogTopic;

    @Min(value = 1L, message = "Invalid datasource id")
    private Long datasourceId;

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> map = super.getParameter();
        map.put("assetsCatalogDomain", assetsCatalogDomain);
        map.put("assetsCatalogTopic", assetsCatalogTopic);
        map.put("datasourceId", datasourceId);
        return map;
    }
}
