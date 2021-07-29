package com.stackstech.honeybee.connector.sqlfilter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class SqlInjectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletRequest res = (HttpServletRequest) servletResponse;
        //获得所有请求参数名
        Enumeration params = req.getParameterNames();
        String sql = "";
        while (params.hasMoreElements()) {
            // 得到参数名
            String name = params.nextElement().toString();
            // 得到参数对应值
            String[] value = req.getParameterValues(name);
            for (int i = 0; i < value.length; i++) {
                sql = sql + value[i];
            }
        }
        if (sqlValidate(sql)) {
            throw new IOException("您发送请求中的参数中含有非法字符");
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 关键词校验
     *
     * @param str
     * @return
     */
    protected static boolean sqlValidate(String str) {
        // 统一转为小写
        str = str.toLowerCase();
        // 过滤掉的sql关键字
        String badStr = "'|and|exec|execute|insert|delete|update|count|drop|*|%|chr|mid|master|truncate|";
//                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
//                "table|grant|use|group_concat|column_name|" +
//                "information_schema.columns|table_schema|union|where|delete|update|order|by|count|*|" +
//                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}

