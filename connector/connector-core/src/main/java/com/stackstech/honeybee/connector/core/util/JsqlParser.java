package com.stackstech.honeybee.connector.core.util;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsqlParser {


    /**
     * 获取字段列表
     *
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> getFields(String sql) throws JSQLParserException {
        final List<Map<String, Object>> fieldList = new ArrayList<>();
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        if (select == null) {
            return null;
        }
        //获取SQL实体类
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        if (plainSelect != null) {
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            if (selectItems != null && selectItems.size() > 0) {
                for (SelectItem selectItem : selectItems) {
                    selectItem.accept(new SelectItemVisitorAdapter() {
                        @Override
                        public void visit(SelectExpressionItem selectExpressionItem) {
                            Map<String, Object> map = new HashMap<>();
                            String fieldName = selectExpressionItem.getExpression().toString();
                            if (fieldName != null && fieldName.contains(".")) {
                                fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1);
                            }
                            map.put("fieldName", (selectExpressionItem.getAlias() == null ?
                                    fieldName : selectExpressionItem.getAlias().getName()));
                            map.put("columnName", selectExpressionItem.getExpression().toString());
                            fieldList.add(map);
                        }
                    });
                }
            }
        }

        return fieldList;
    }

    /**
     * 获取参数列表<无法解析$字符>
     *
     * @param sql
     * @return
     */
    public static String getParams(String sql) throws JSQLParserException {
        List<Map<String, Object>> paramList = new ArrayList<>();
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        if (select == null) {
            return null;
        }
        //获取SQL实体类
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        if (plainSelect != null) {
            Expression expression = plainSelect.getWhere();
            return expression.toString();
        }

        return null;
    }


}
