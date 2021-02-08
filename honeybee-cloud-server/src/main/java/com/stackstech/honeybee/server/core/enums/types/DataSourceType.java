package com.stackstech.honeybee.server.core.enums.types;

import com.fasterxml.jackson.annotation.JsonValue;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;

/**
 * DB data source type
 *
 * @author william
 * @since 1.0
 */
public enum DataSourceType implements BaseEnumTypeService {
    /**
     * Hive
     */
    HIVE("Hive", "HIVE"),
    /**
     * MySQL
     */
    MYSQL("MySQL", "MYSQL"),
    /**
     * MSSQL
     */
    MSSQL("MSSQL", "MSSQL"),
    /**
     * Oracle
     */
    ORACLE("Oracle", "ORACLE"),
    /**
     * PostgreSQL
     */
    POSTGRESQL("PostgreSQL", "POSTGRESQL");


    private final String name;
    private final String code;

    @Override
    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    DataSourceType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
