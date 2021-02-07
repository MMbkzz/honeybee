package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MYSQL DB JSON Type handler
 *
 * @author William
 * @since 1.0
 */
@Slf4j
@MappedTypes(JsonParameterMap.class)
@MappedJdbcTypes(JdbcType.LONGVARCHAR)
public class JsonTypeHandler extends BaseTypeHandler<JsonParameterMap> {

    protected JsonParameterMap parse(String result) {
        if (StringUtils.isNotBlank(result)) {
            log.debug("The result is not empty, parse the JSON data into object");
            return CommonUtil.jsonToObject(result, JsonParameterMap.class);
        }
        log.debug("The result is empty, return null now");
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonParameterMap parameter, JdbcType jdbcType) throws SQLException {
        log.debug("The result is not empty, parse the object as JSON data");
        ps.setString(i, CommonUtil.toJsonString(parameter));
    }

    @Override
    public JsonParameterMap getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public JsonParameterMap getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public JsonParameterMap getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

}