package com.stackstech.honeybee.server.system.vo;

import com.stackstech.honeybee.common.vo.PageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@ApiModel
@Data
@NotNull(message = "query parameter cannot be null")
public class DataSourceQuery extends PageQuery {

    private String datasourceType;

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> map = super.getParameter();
        map.put("datasourceType", datasourceType);
        return map;
    }
}
