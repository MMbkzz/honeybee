package com.stackstech.honeybee.server.audit.vo;

import com.stackstech.honeybee.common.vo.PageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@ApiModel
@Data
public class AuditLogQuery extends PageQuery {

    private String logType;

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> map = super.getParameter();
        map.put("logType", StringUtils.trimToNull(logType));
        return map;
    }
}
