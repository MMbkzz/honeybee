package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.enums.types.MessageType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Message enum type handler
 *
 * @author william
 * @see BaseEnumTypeHandler
 * @see MessageType
 * @since 1.0
 */
@MappedTypes(MessageType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MessageEnumTypeHandler extends BaseEnumTypeHandler<MessageType> {

    public MessageEnumTypeHandler(Class<MessageType> type) {
        super(type);
    }

}
