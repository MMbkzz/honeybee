package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.common.utils.CommonUtil;
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
@MappedTypes(JsonParameterMap.class)
@MappedJdbcTypes(JdbcType.LONGVARCHAR)
public class JsonTypeHandler extends BaseTypeHandler<JsonParameterMap> {

    protected JsonParameterMap parse(String result) {
        if (StringUtils.isNotBlank(result)) {
            return CommonUtil.jsonToObject(result, JsonParameterMap.class);
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonParameterMap parameter, JdbcType jdbcType) throws SQLException {
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