package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.enums.types.DataSourceType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * DB source enum type handler
 *
 * @author william
 * @see BaseEnumTypeHandler
 * @see DataSourceType
 * @since 1.0
 */
@MappedTypes(DataSourceType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class DBEnumTypeHandler extends BaseEnumTypeHandler<DataSourceType> {

    public DBEnumTypeHandler(Class<DataSourceType> type) {
        super(type);
    }

}
