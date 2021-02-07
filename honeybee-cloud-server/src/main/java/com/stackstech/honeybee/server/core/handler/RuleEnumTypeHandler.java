package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.enums.types.QualityRuleType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Quality rule enum type handler
 *
 * @author william
 * @see BaseEnumTypeHandler
 * @see QualityRuleType
 * @since 1.0
 */
@MappedTypes(QualityRuleType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class RuleEnumTypeHandler extends BaseEnumTypeHandler<QualityRuleType> {

    public RuleEnumTypeHandler(Class<QualityRuleType> type) {
        super(type);
    }

}
