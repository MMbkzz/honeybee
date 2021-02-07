package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.enums.types.AuditOperationType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Audit enum type handler
 *
 * @author william
 * @see BaseEnumTypeHandler
 * @see AuditOperationType
 * @since 1.0
 */
@MappedTypes(AuditOperationType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class AuditEnumTypeHandler extends BaseEnumTypeHandler<AuditOperationType> {

    public AuditEnumTypeHandler(Class<AuditOperationType> type) {
        super(type);
    }

}
