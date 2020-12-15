package com.stackstech.dcp.connector.core.util;

import com.stackstech.dcp.connector.core.enums.DriverTypesEnum;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL解析工具类
 */
public class SqlParser {

    /**
     * 获取SQL语句中参数列表
     *
     * @param sql select aa
     *            aa as f1,bb as f2
     *            max(aa) as f3,max(bb) f4, bb f5
     *            from table where 1=1
     *            and p1 = ${p1}
     *            <#if p2??> and p2 > ${p2}</#if>
     *            <#if p3??> and p3 = ${p3}</#if>
     * @return
     */
    public static Map<String, List<Map<String, Object>>> getParams(String sql) throws Exception {

        Map<String, List<Map<String, Object>>> fieldsAndParams = new HashMap<>();
        List<Map<String, Object>> fieldList = new ArrayList<>();
        List<Map<String, Object>> paramList = new ArrayList<>();
        if (sql != null) {
            sql = sql.replaceAll("\n", " ");
            //解析字段表达式
            String fieldSQL = sql.toLowerCase().substring(0, sql.toLowerCase().indexOf(" from "));
            int placeIndex = fieldSQL.indexOf("$");
            int ifIndex = fieldSQL.indexOf("<#");
            if (ifIndex != -1 && placeIndex != -1) {
                if (placeIndex < ifIndex) {
                    fieldSQL = fieldSQL.replaceAll("(\\$\\{)(.+?)(\\})", "aa");
                    if (fieldSQL.contains("<#")) {
                        fieldSQL = fieldSQL.substring(0, fieldSQL.indexOf("<#")).trim();
                    }
                } else {
                    fieldSQL = fieldSQL.substring(0, fieldSQL.indexOf("<#")).trim();
                }
            } else {
                if (placeIndex != -1) {
                    fieldSQL = fieldSQL.replaceAll("(\\$\\{)(.+?)(\\})", "aa");
                } else if (ifIndex != -1) {
                    fieldSQL = fieldSQL.substring(0, fieldSQL.indexOf("<#")).trim();
                }
            }
            //解析参数表达式
            List<String> paramArray = new ArrayList<>();
            String[] paramSplits = sql.toLowerCase().split("(?i)where");
            if (paramSplits.length == 1) {
                paramArray.addAll(Arrays.asList(paramSplits[0].split(("(?i)and "))));
            } else {
                if (paramSplits.length > 1) {
                    for (int i = 1; i < paramSplits.length; i++) {
                        /**
                         * OR字段逻辑<如有问题，请注释掉这段代码>
                         */
                        List<String> allParams = new ArrayList<String>();
                        String[] whereArray = paramSplits[i].split(("(?i)and "));
                        for (String whereStr : whereArray) {
                            if (whereStr.contains("or ") || whereStr.contains("OR ")) {
                                if (whereStr.trim().startsWith("(") && whereStr.trim().endsWith(")")) {
                                    whereStr = whereStr.trim().substring(1, whereStr.trim().length() - 2);
                                    String[] orArray = whereStr.split(("(?i)or "));
                                    allParams.addAll(Arrays.asList(orArray));
                                } else {
                                    String[] orArray = whereStr.split(("(?i)or "));
                                    allParams.addAll(Arrays.asList(orArray));
                                }
                            } else {
                                allParams.add(whereStr);
                            }
                        }
                        paramArray.addAll(allParams);//OR字段逻辑结束
                        //paramArray.addAll(Arrays.asList(paramSplits[i].split(("(?i)and "))));
                    }
                }
            }
            //获取字段列表
            fieldList = JsqlParser.getFields(fieldSQL);
            if (fieldList != null && fieldList.size() > 0) {
                for (int i = 0; i < fieldList.size(); i++) {
                    Map<String, Object> map = fieldList.get(i);
                    map.put("expression", "$" + (i + 1));
                    map.put("isDerived", "n");
                }
            }
            //获取参数列表
            if (paramArray != null && paramArray.size() > 0) {
                for (String param : paramArray) {
                    if (!param.contains("$")) {
                        continue;
                    }
                    String spaceParam = param.trim();
                    Map<String, Object> map = new HashMap<>();
                    String paramName = spaceParam.substring(spaceParam.indexOf("{") + 1, spaceParam.indexOf("}"));
                    map.put("paramName", paramName.trim());
                    map.put("fieldName", spaceParam.split(" ")[0]);
                    if (spaceParam.contains("</#if>")) {
                        map.put("isRequired", "n");
                    } else {
                        map.put("isRequired", "y");
                    }
                    paramList.add(map);
                }
            }
        }
        fieldsAndParams.put("fieldList", fieldList);
        fieldsAndParams.put("paramList", paramList);
        return fieldsAndParams;
    }

    /**
     * 解析驱动类型
     *
     * @param driverClass
     * @return
     */
    public static DriverTypesEnum parseDriverType(String driverClass) {
        if (driverClass != null && !"".equals(driverClass)) {
            if (driverClass.contains("Mysql")) {
                return DriverTypesEnum.MYSQL;
            } else if (driverClass.contains("Oracle")) {
                return DriverTypesEnum.ORACLE;
            } else if (driverClass.contains("SqlServer")) {
                return DriverTypesEnum.SQLSERVER;
            } else if (driverClass.contains("Hbase")) {
                return DriverTypesEnum.HBASE;
            } else if (driverClass.contains("Hive")) {
                return DriverTypesEnum.HIVE;
            } else if (driverClass.contains("Redis")) {
                return DriverTypesEnum.REDIS;
            } else if (driverClass.contains("PostgreSQL")) {
                return DriverTypesEnum.POSTGRESQL;
            } else if (driverClass.contains("Kafka")) {
                return DriverTypesEnum.KAFKA;
            }
        }
        return null;
    }

    /**
     * 解析驱动对应模型类型
     *
     * @param driverClass
     * @return
     */
    public static String parseModelType(String driverClass) {
        if (driverClass != null && !"".equals(driverClass)) {
            if (driverClass.contains("Mysql")) {
                return "data";
            } else if (driverClass.contains("Oracle")) {
                return "data";
            } else if (driverClass.contains("SqlServer")) {
                return "data";
            } else if (driverClass.contains("Hbase")) {
                return "data";
            } else if (driverClass.contains("Hive")) {
                return "data";
            } else if (driverClass.contains("Redis")) {
                return "nosql";
            } else if (driverClass.contains("PostgreSQL")) {
                return "data";
            } else if (driverClass.contains("Kafka")) {
                return "data";
            }
        }
        return null;
    }

    /**
     * 获取数据表SQL
     *
     * @param driverClass
     * @return
     */
    public static String getTableSQL(String driverClass, String queryString) throws Exception {
        String sql = null;
        DriverTypesEnum driverType = parseDriverType(driverClass);
        if (driverType != null) {
            switch (driverType) {
                case MYSQL:
                    sql = "show tables";
                    break;
                case ORACLE:
                    String oracleString = queryString != null ? "AND UPPER(T.OWNER) ||  '.' || UPPER(T.OBJECT_NAME) LIKE UPPER('%" + queryString + "%')" : " ";
                    sql = "SELECT UPPER(T.OWNER) ||  '.' || UPPER(T.OBJECT_NAME) AS TABLE_NAME" +
                            " FROM ALL_OBJECTS T WHERE" +
                            " T.OBJECT_TYPE IN('TABLE','VIEW')" +
                            " AND T.OWNER NOT LIKE 'SYS%'" + oracleString +
                            " AND ROWNUM<=100" +
                            " ORDER BY T.OWNER,T.OBJECT_NAME";
                    break;
                case SQLSERVER:
                    sql = "select name from sys.tables";
                    break;
                case HIVE:
                    sql = "show tables";
                    break;
                case HBASE:
                    sql = "hbase";
                    break;
                case POSTGRESQL:
                    String pgString1 = (queryString != null ? "AND UPPER( T.SCHEMANAME) ||  '.' || UPPER(T.TABLENAME) LIKE UPPER('%" + queryString + "%')" : " ");
                    String pgString2 = (queryString != null ? "AND UPPER( T.SCHEMANAME) ||  '.' || UPPER(T.VIEWNAME) LIKE UPPER('%" + queryString + "%')" : " ");
                    sql = "SELECT T.TABLE_NAME" +
                            " FROM" +
                            " (SELECT T.SCHEMANAME,T.TABLENAME,UPPER( T.SCHEMANAME) ||  '.' || UPPER(T.TABLENAME) AS TABLE_NAME" +
                            "  FROM PG_TABLES T" +
                            "  WHERE  UPPER(T.SCHEMANAME) NOT LIKE UPPER('PG%')" + pgString1 +
                            "  UNION ALL " +
                            "  SELECT T.SCHEMANAME,T.VIEWNAME AS TABLENAME,UPPER( T.SCHEMANAME) ||  '.' || UPPER(T.VIEWNAME) AS TABLE_NAME" +
                            "  FROM PG_VIEWS T" +
                            "  WHERE  UPPER(T.VIEWNAME) NOT LIKE UPPER('PG%')" + pgString2 +
                            "  ) T" +
                            " ORDER BY T.SCHEMANAME,T.TABLENAME  LIMIT 100";
                    break;
                default:
                    break;
            }
        }
        return sql;
    }

    /**
     * 获取字段SQL<需要过滤表名称></>
     *
     * @param driverClass
     * @return
     */
    public static String getFiledsSQL(String driverClass, String tableName, Map<String, Object> properties) {
        String sql = null;
        DriverTypesEnum driverType = parseDriverType(driverClass);
        if (driverType != null) {
            switch (driverType) {
                case MYSQL:
                    sql = "select COLUMN_NAME fieldName, DATA_TYPE dataType, COLUMN_COMMENT fieldDesc " +
                            " from INFORMATION_SCHEMA.COLUMNS where table_name = '" + tableName + "'" +
                            " and table_schema = '" + getDbName(driverType, properties) + "'";
                    break;
                case ORACLE:
                    //切分表名称|owner名称
                    String owner = tableName.contains(".") ? tableName.substring(0, tableName.lastIndexOf(".")) : tableName;
                    tableName = tableName.contains(".") ? tableName.substring(tableName.lastIndexOf(".") + 1) : tableName;
                    sql = "select b.COLUMN_NAME as fieldName,b.DATA_TYPE as dataType,a.COMMENTS as fieldDesc" +
                            " from ALL_TAB_COLUMNS b,ALL_COL_COMMENTS a where " +
                            " b.TABLE_NAME = '" + tableName + "' and b.OWNER = '" + owner + "' " +
                            " and b.TABLE_NAME = a.TABLE_NAME and b.COLUMN_NAME = a.COLUMN_NAME " +
                            " order by b.COLUMN_ID";
                    break;
                case SQLSERVER:
                    sql = "select sys.columns.name as fieldName,sys.types.name as dataType, " +
                            " (select convert(nvarchar(200),sys.extended_properties.value) value from sys.extended_properties " +
                            " where sys.extended_properties.major_id = sys.columns.object_id " +
                            " and sys.extended_properties.minor_id = sys.columns.column_id) as fieldDesc " +
                            " from sys.columns, sys.tables, sys.types where sys.columns.object_id = sys.tables.object_id " +
                            " and sys.columns.system_type_id=sys.types.system_type_id and sys.tables.name= '" + tableName + "' " +
                            " order by sys.columns.column_id";
                    break;
                case HIVE:
                    sql = "desc " + tableName;
                    break;
                case HBASE:
                    sql = tableName;
                    break;
                case POSTGRESQL:
                    tableName = (tableName != null ? tableName.substring(tableName.lastIndexOf(".") + 1) : null);
                    tableName = (tableName != null ? tableName.toLowerCase() : null);  //转换成小写
                    sql = "SELECT a.attname as fieldName,t.typname as dataType,b.description as fieldDesc" +
                            " from pg_class c,pg_attribute a LEFT OUTER JOIN pg_description b ON a.attrelid=b.objoid AND a.attnum = b.objsubid,pg_type t" +
                            " WHERE c.relname = '" + tableName + "' and a.attnum > 0  and a.attrelid = c.oid and a.atttypid = t.oid  ORDER BY a.attnum";
                    break;
                default:
                    break;
            }
        }
        return sql;
    }

    /**
     * 获取数据库名称
     *
     * @param type
     * @param properties
     * @return
     */
    private static String getDbName(DriverTypesEnum type, Map<String, Object> properties) {
        if (properties == null || properties.isEmpty()) {
            return null;
        }
        if (DriverTypesEnum.HIVE == type || DriverTypesEnum.MYSQL == type) {
            for (String key : properties.keySet()) {
                if (key.contains("url")) {
                    String value = properties.get(key) != null ? (String) properties.get(key) : null;
                    if (value != null) {
                        if (value.contains("?")) {
                            return value.substring(value.lastIndexOf("/") + 1, value.indexOf("?")).trim();
                        } else {
                            return value.substring(value.lastIndexOf("/") + 1).trim();
                        }
                    }
                    break;
                }
            }
        }
        if (DriverTypesEnum.SQLSERVER == type) {
            for (String key : properties.keySet()) {
                if (key.contains("url")) {
                    String value = properties.get(key) != null ? (String) properties.get(key) : null;
                    if (value != null) {
                        if (value.contains("?")) {
                            return value.substring(value.lastIndexOf("=") + 1, value.indexOf("?")).trim();
                        } else {
                            return value.substring(value.lastIndexOf("=") + 1).trim();
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 解析字段
     *
     * @param driverClass
     * @param lists
     * @return
     */
    public static List<Map<String, Object>> parseTableFields(String driverClass, List<Map<String, Object>> lists) {
        List<Map<String, Object>> fields = new ArrayList<>();
        DriverTypesEnum driverType = SqlParser.parseDriverType(driverClass);
        if (driverClass != null && lists != null) {
            switch (driverType) {
                case HIVE:
                    for (Map<String, Object> field : lists) {
                        if (field != null && null != field.get("col_name")) {
                            Map<String, Object> map = new HashMap<>();
                            String colName = String.valueOf(field.get("col_name"));
                            if (!"".equals(colName) && !colName.startsWith("#")) {
                                map.put("fieldName", field.get("col_name"));
                                map.put("dataType", field.get("data_type"));
                                map.put("fieldDesc", field.get("comment"));
                                fields.add(map);
                            }
                        }
                    }
                    //分区字段去重
                    if (lists != null && lists.size() > 0) {
                        Set h = new HashSet(fields);
                        fields.clear();
                        fields.addAll(h);
                    }
                    break;
                case HBASE:
                    for (Map<String, Object> field : lists) {
                        if (null != field && null != field.get("col_name")) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("fieldName", field.get("col_name"));
                            map.put("dataType", "");
                            map.put("fieldDesc", "");
                            fields.add(map);
                        }
                    }
                    break;
                case POSTGRESQL:
                    for (Map<String, Object> field : lists) {
                        if (field != null && null != field.get("fieldname")) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("fieldName", field.get("fieldname"));
                            map.put("dataType", field.get("datatype"));
                            map.put("fieldDesc", field.get("fielddesc"));
                            fields.add(map);
                        }
                    }
                    break;
                case ORACLE:
                    for (Map<String, Object> field : lists) {
                        if (field != null && null != field.get("FIELDNAME")) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("fieldName", field.get("FIELDNAME"));
                            map.put("dataType", field.get("DATATYPE"));
                            map.put("fieldDesc", field.get("FIELDDESC"));
                            fields.add(map);
                        }
                    }
                    break;
                default:
                    fields = lists;
                    break;
            }
            if (fields != null && fields.size() > 0) {
                for (Map<String, Object> map : fields) {
                    map.put("expression", "$" + (fields.indexOf(map) + 1));
                    map.put("isDerived", "n");
                }
            }
        }
        return fields;
    }


    /**
     * 解析$占位符
     *
     * @param fields
     * @param expression
     * @return
     */
    public static String parseExperssion(List<Map<String, Object>> fields, StringBuffer expression) {
        if (expression != null && !"".equals(expression)) {
            List<String> placeholders = getExpression(expression.toString());
            if (placeholders != null && placeholders.size() > 0) {
                for (String placeholder : placeholders) {
                    if (placeholder != null && !"".equals(placeholder)) {
                        for (Map<String, Object> fieldMap : fields) {
                            String expre = (String) fieldMap.get("expression");
                            String isDerived = (String) fieldMap.get("isDerived");
                            if (!"y".equals(isDerived) && placeholder.equals(expre)) {
                                String fieldName = (String) fieldMap.get("fieldName");
                                int index = expression.indexOf(expre);
                                expression = expression.replace(index, (index + expre.length()), fieldName);
                                break;
                            }
                        }
                    }
                }
            }
            return expression.toString();
        }
        return null;
    }


    /**
     * 获取占位符
     *
     * @param expression
     */
    private static List<String> getExpression(String expression) {
        List<String> expres = new ArrayList<>();

        String regEx = "\\$[1-9]+[0-9]*";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            expres.add(matcher.group());
        }
        return expres;
    }

    /**
     * 解析时间字段
     *
     * @param driverClass
     */
    public static String parseDate(String driverClass, String expression) {
        String dateParam = null;
        DriverTypesEnum driverType = SqlParser.parseDriverType(driverClass);
        if (driverType != null) {
            switch (driverType) {
                case MYSQL:
                    dateParam = "${" + expression + "}";
                    break;
                case SQLSERVER:
                    dateParam = "${" + expression + "}";
                    break;
                case ORACLE:
                    dateParam = "to_date(${" + expression + "},'YYYY-MM-DD HH24:MI:SS')";
                    break;
                case HIVE:
                    dateParam = "${" + expression + "}";
                    break;
                case POSTGRESQL:
                    dateParam = "${" + expression + "}";
                    break;
                default:
                    break;
            }
        }
        return dateParam;
    }

    /**
     * 针对不同种类的SQL现在返回行数
     *
     * @param sql
     * @return
     */
    public static String limitSql(String driverClass, String sql, int limitNum) {
        String afterSql = null;
        DriverTypesEnum driverType = SqlParser.parseDriverType(driverClass);
        if (driverType != null) {
            switch (driverType) {
                case MYSQL:
                    afterSql = sql + " limit  " + limitNum;
                    break;
                case SQLSERVER:
                    afterSql = sql.replaceFirst("select", "select top " + limitNum);
                    break;
                case ORACLE:
                    afterSql = "select * from ( " + sql + " ) where rowNum <= " + limitNum;
                    break;
                case HIVE:
                    afterSql = sql + " limit  " + limitNum;
                    break;
                case POSTGRESQL:
                    afterSql = sql + " limit  " + limitNum;
                    break;
                default:
                    break;
            }
        }
        return afterSql;
    }

    public static void main(String[] args) throws Exception {
        /*List<Map<String, Object>> fields = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("expression", "$1");
        map1.put("fieldName", "ID");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("expression", "$2");
        map2.put("fieldName", "NAME");

        Map<String, Object> map3 = new HashMap<>();
        map3.put("expression", "$3");
        map3.put("fieldName", "AGE");

        Map<String, Object> map4 = new HashMap<>();
        map4.put("expression", "$1");
        map4.put("fieldName", "AGE11");

        Map<String, Object> map5 = new HashMap<>();
        map5.put("expression", "$1+$2");
        map5.put("fieldName", "AGE22");

        Map<String,Object> map6 = new HashMap<>();
        map6.put("expression", "$2");
        map6.put("fieldName", "NAME");

        fields.add(map1);
        fields.add(map2);
        fields.add(map3);
        fields.add(map4);
        fields.add(map5);
        fields.add(map6);

        System.out.println(parseExperssion(fields, new StringBuffer("$1+$2")));*/

        String sql = "SELECT t.rownum" +
                "  FROM (SELECT rownum                      rownum_, " +
                "               acs.id                      shop_id," +
                "               acs.store_id                shop_no, " +
                "               acs.store_name              shop_name," +
                "               acs.store_handover_status, " +
                "               acs.act_pos_no,  " +
                "               acs.busi_type, " +
                "               acs.sub_bus_type,  " +
                "               acs.key_cashier_type,    " +
                "               acs.report_type, " +
                "               acs.kechuan_start_date,  " +
                "               acs.kechuan_end_date,    " +
                "               acs.key_store_floor         store_floor_id, " +
                "               sft.floor_code              store_floor_code,  " +
                "               sft.floor_name              store_floor_name, " +
                "               aci.contract_id, " +
                "               acs.attribute18             project_number, " +
                "               aci.contract_n_o            contract_no, " +
                "               aci.contract_name,  " +
                "               aci.business_type, " +
                "               aci.finance_confirm_status, " +
                "               aci.contract_status, " +
                "               aci.c_verified, " +
                "               aci.c_approved, " +
                "               aci.key_cooperation_mode, " +
                "               aci.contract_rel, " +
                "               aci.contract_effe_date_from, " +
                "               aci.contract_effe_date_end, " +
                "               aci.contract_object_type, " +
                "               aci.customer_id, " +
                "               cus.customer_code, " +
                "               cus.customer_name " +
                "          FROM agm_contract_shoppe_info     acs," +
                "               agm_contract_instance_object aci," +
                "               com_sieble_customers_v       cus," +
                "               sps_floor_tab                sft" +
                "         WHERE 1 = 1" +
                "           AND acs.attribute18 = ${attribute18}" +
                "           AND (acs.last_update_date >= to_date('${last_update_date1}', 'YYYYMMDD') OR " +
                "               aci.last_update_date >= to_date('${last_update_date2}', 'YYYYMMDD')) " +
                "           AND cus.customer_id(+) = aci.customer_id" +
                "           AND aci.selected_used(+) = 1" +
                "           AND aci.shop_no(+) = to_char(acs.id)" +
                "           AND to_char(sft.floor_id(+)) = acs.key_store_floor" +
                "           AND rownum < ${rownumEnd}) t " +
                " WHERE 1 = 1" +
                "   AND t.rownum_ >= ${rownumStart} ";

        String ss1 = "SELECT alias, name, legacy_id, industry, sub_industry " +
                "FROM brand where 1=1 (<#if alias??> and alias like ${alias}</#if> <#if name??> or name like ${name}</#if>) " +
                "<#if legacy_id??>and legacy_id = ${legacy_id}</#if>";
        //System.out.println(sql);

        SqlParser.getParams(ss1);

    }
}
