package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.enums.types.EntityStatusType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Status enum type handler
 *
 * @author william
 * @see BaseEnumTypeHandler
 * @see EntityStatusType
 * @since 1.0
 */
@MappedTypes(EntityStatusType.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class StatusEnumTypeHandler extends BaseEnumTypeHandler<EntityStatusType> {

    public StatusEnumTypeHandler(Class<EntityStatusType> type) {
        super(type);
    }

}
