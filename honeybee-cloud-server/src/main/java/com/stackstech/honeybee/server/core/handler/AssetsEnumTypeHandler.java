package com.stackstech.honeybee.server.core.handler;

import com.stackstech.honeybee.server.core.enums.types.AssetsCatalogType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Assets enum type handler
 *
 * @author william
 * @see BaseEnumTypeHandler
 * @see AssetsCatalogType
 * @since 1.0
 */
@MappedTypes(AssetsCatalogType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class AssetsEnumTypeHandler extends BaseEnumTypeHandler<AssetsCatalogType> {

    public AssetsEnumTypeHandler(Class<AssetsCatalogType> type) {
        super(type);
    }

}
