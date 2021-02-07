package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Base enum type handler
 *
 * @author william
 * @see BaseEnumTypeService
 * @see BaseTypeHandler
 * @since 1.0
 */
public class BaseEnumTypeHandler<E extends Enum<E> & BaseEnumTypeService> extends BaseTypeHandler<E> {

    private final Class<E> type;
    private final E[] enums;

    public BaseEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType.TYPE_CODE == JdbcType.INTEGER.TYPE_CODE) {
            ps.setInt(i, Integer.valueOf(parameter.getCode()));
        } else {
            ps.setString(i, parameter.getCode());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getEnum(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getEnum(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getEnum(cs.getString(columnIndex));
    }

    private E getEnum(String code) {
        try {
            if (StringUtils.isEmpty(code)) {
                return null;
            }
            return Arrays.stream(enums).filter(
                    e -> e.getCode().equalsIgnoreCase(code)
            ).findFirst().orElse(null);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + code + " to " + type.getSimpleName() + " by ordinal value.", ex);
        }
    }
}
