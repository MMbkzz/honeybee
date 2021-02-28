package com.stackstech.honeybee.server.core.enums.types;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stackstech.honeybee.server.core.service.BaseEnumTypeService;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class EnumTypeMapping {

    private static final Map<String, List<EnumTypeMapping>> ENUM_TYPE_MAPPING = Maps.newConcurrentMap();

    static {
        ENUM_TYPE_MAPPING.put("ASSETS_CATALOG_TYPE", of(AssetsCatalogType.values()));
        List<EnumTypeMapping> logTypeStatus = Lists.newArrayList();
        logTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.DELETE));
        logTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.INSERT));
        logTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.UPDATE));
        logTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.ERROR));
        ENUM_TYPE_MAPPING.put("LOG_TYPE", logTypeStatus);
        List<EnumTypeMapping> sysLogTypeStatus = Lists.newArrayList();
        sysLogTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.DELETE));
        sysLogTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.INSERT));
        sysLogTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.UPDATE));
        sysLogTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.LOGIN));
        sysLogTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.LOGOUT));
        sysLogTypeStatus.add(new EnumTypeMapping().build(AuditOperationType.ERROR));
        ENUM_TYPE_MAPPING.put("SYS_LOG_TYPE", sysLogTypeStatus);
        ENUM_TYPE_MAPPING.put("DATA_SOURCE_TYPE", of(DataSourceType.values()));
        List<EnumTypeMapping> status = Lists.newArrayList();
        status.add(new EnumTypeMapping().build(EntityStatusType.ENABLE));
        status.add(new EnumTypeMapping().build(EntityStatusType.DISABLE));
        ENUM_TYPE_MAPPING.put("STATUS", status);
        List<EnumTypeMapping> serviceStatus = Lists.newArrayList();
        serviceStatus.add(new EnumTypeMapping().build(EntityStatusType.ENABLE));
        serviceStatus.add(new EnumTypeMapping().build(EntityStatusType.DISABLE));
        serviceStatus.add(new EnumTypeMapping().build(EntityStatusType.ONLINE));
        serviceStatus.add(new EnumTypeMapping().build(EntityStatusType.OFFLINE));
        ENUM_TYPE_MAPPING.put("SERVICE_STATUS", serviceStatus);
        ENUM_TYPE_MAPPING.put("MESSAGE_TYPE", of(MessageType.values()));
        ENUM_TYPE_MAPPING.put("QUALITY_RULE_TYPE", of(QualityRuleType.values()));
    }

    private String name;
    private String code;

    public EnumTypeMapping build(BaseEnumTypeService enumType) {
        this.name = enumType.getName();
        this.code = enumType.getCode();
        return this;
    }

    public static List<EnumTypeMapping> of(BaseEnumTypeService[] enums) {
        List<EnumTypeMapping> mappings = Lists.newArrayList();
        Arrays.stream(enums).forEach(e -> mappings.add(new EnumTypeMapping().build(e)));
        return mappings;
    }

    public static Map<String, List<EnumTypeMapping>> getMapping() {
        return ENUM_TYPE_MAPPING;
    }

}
