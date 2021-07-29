package com.stackstech.honeybee.connector.core.util;

import freemarker.core.TemplateClassResolver;
import freemarker.core.TemplateConfiguration;
import freemarker.core.UndefinedOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.management.openmbean.SimpleType;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;


/**
 * 解析sql表达式模板
 */
public class SqlTemplateUtil {

//    private static SimpleType DataTypeEnum;

    /**
     * sql表达式解析成sql
     *
     * @param sqlExpression
     * @throws IOException
     * @throws TemplateException
     */
    public static String parse(String sqlExpression, Map<String, Object> mapValue) throws IOException, TemplateException {

        Configuration freeMarkerConfig = initConfig();
        TemplateConfiguration tCfg = new TemplateConfiguration();
        tCfg.setParentConfiguration(freeMarkerConfig);

        Template template = new Template(null, null,
                new StringReader(sqlExpression), freeMarkerConfig, tCfg, null);
        tCfg.apply(template);

        StringWriter writer = new StringWriter();
        template.process(mapValue, new LengthLimitedWriter(writer, 100000));

        return writer.toString();
    }

    /**
     * 根据类型解析defaultValue
     *
     * @param values
     * @return
     */
    public static Map<String, Object> parseValues(List<Map<String, Object>> values) {
        Map<String, Object> mapValue = new HashMap<>();
        if (values != null && values.size() > 0) {
            for (Map<String, Object> map : values) {
                String name = String.valueOf(map.get("name"));
                String value = (map.get("value") != null ? String.valueOf(map.get("value")) : null);
                String type = String.valueOf(map.get("type"));
                if (value != null && !"".equals(value)) {
                    if (DataTypeEnum.STRING.toString().equalsIgnoreCase(type) || DataTypeEnum.DATE.toString().equalsIgnoreCase(type)) {
                        value = "'" + value + "'";
                    }
                    mapValue.put(name, value);
                }
            }
        }
        return mapValue;
    }

    /**
     * 生成SQL表达式模板<Table>
     *
     * @param fields
     * @param params
     * @param tableName
     * @return
     */
    public static String create(List<Map<String, Object>> fields, List<Map<String, Object>> params, String driverClass, String tableName) throws Exception {
        StringBuffer buffer = new StringBuffer();
        //解析字段列表
        if (fields != null && fields.size() > 0) {
            buffer.append("select ");
            for (Map<String, Object> fieldMap : fields) {
                String isDerived = (String) fieldMap.get("isDerived");
                String expression = (String) fieldMap.get("expression");
                String fieldName = (String) fieldMap.get("fieldName");
                if ("y".equals(isDerived)) {
                    StringBuffer str = new StringBuffer(expression);
                    expression = SqlParser.parseExperssion(fields, str);
                    if (expression != null) {
                        buffer.append(" " + expression + " as " + fieldName + ",");
                    }
                } else {
                    buffer.append(" " + fieldName + ",");
                }
            }
            buffer = buffer.deleteCharAt(buffer.length() - 1);
            buffer.append(" from " + tableName + " where 1 = 1 ");
        }
        //解析属性列表
        if (params != null && params.size() > 0) {
            for (Map<String, Object> paramMap : params) {
                String isRequired = (String) paramMap.get("isRequired");
                String paramName = (String) paramMap.get("paramName");
                String fieldName = (String) paramMap.get("fieldName");
                String operateType = (String) paramMap.get("operateType");
                String paramType = (String) paramMap.get("paramType");
                if (paramName != null && operateType != null) {
                    //解析填充字段
                    String placeHolder = "${" + paramName + "}";
                    if ("date".equalsIgnoreCase(paramType)) {
                        placeHolder = SqlParser.parseDate(driverClass, paramName);
                    }
                    if (isRequired != null && "n".equalsIgnoreCase(isRequired)) {
                        buffer.append(" <#if ").append(paramName).append("??>")
                                .append(" and " + fieldName).append(" " + operateType + " ").append(placeHolder)
                                .append(" </#if> ");
                    } else {
                        buffer.append(" and " + fieldName).append(" " + operateType + " ").append(placeHolder);
                    }
                }
            }
        }
        return buffer.toString();
    }


    /**
     * 去除List中的属性
     *
     * @param result
     * @param filedMap
     * @return
     */
    public static List<Map<String, Object>> removeField(List<Map<String, Object>> result, Map<String, Object> filedMap) {

        List<Map<String, Object>> resultList = new ArrayList<>();

        if (filedMap == null) {
            return result;
        }
        result.forEach(m -> {
            Map<String, Object> hashMap = new HashMap<>();
            for (String key : filedMap.keySet()) {
                if (m.get(key) == null || m.get(key) == "" || "null".equalsIgnoreCase(m.get(key).toString())) {
                    hashMap.put(key, "");
                } else {
                    hashMap.put(key, m.get(key));
                }
            }
            resultList.add(hashMap);
        });
        return resultList;
    }


    private static Configuration initConfig() {
        Configuration freeMarkerConfig = new Configuration(Configuration.getVersion());
        freeMarkerConfig.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
        freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freeMarkerConfig.setLogTemplateExceptions(false);
        freeMarkerConfig.setAttemptExceptionReporter((te, env) -> {
            // Suppress it?
        });
        freeMarkerConfig.setLocale(Locale.CHINA);
        freeMarkerConfig.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        freeMarkerConfig.setOutputFormat(UndefinedOutputFormat.INSTANCE);
        freeMarkerConfig.setOutputEncoding("UTF-8");

        return freeMarkerConfig;
    }

}
