package com.stackstech.honeybee.server.core.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NotNull(message = "query parameter cannot be null")
public class AuditLogQuery extends PageQuery {

    private String logType;

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> map = super.getParameter();
        map.put("logType", logType);
        return map;
    }
}
