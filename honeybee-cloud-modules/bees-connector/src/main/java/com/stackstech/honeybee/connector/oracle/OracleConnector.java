package com.stackstech.honeybee.connector.oracle;


import com.stackstech.honeybee.connector.core.util.SqlParser;
import com.stackstech.honeybee.connector.core.util.SqlTemplateUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OracleConnector extends AbstractBasicDataSourceConnector implements ResourceSession {

    private JdbcTemplate jdbcTemplate;

    private OracleConnector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static OracleConnector getInstance(ClassLoader classLoader) {
        return new OracleConnector(classLoader);
    }

    public OracleConnector initialize(Map<String, Object> config) {
        dataSource = new BasicDataSource();
        dataSource.setUrl((String) config.get("oracle.datasource.url"));
        dataSource.setUsername((String) config.get("oracle.datasource.username"));
        dataSource.setPassword((String) config.get("oracle.datasource.password"));
        dataSource.setDriverClassName((String) config.get("oracle.datasource.driver-class-name"));

        if (config.get("oracle.datasource.query-timeout") != null) {
            dataSource.setDefaultQueryTimeout(Integer.valueOf((String) config.get("oracle.datasource.query-timeout")));
        }

        if (config.get("oracle.datasource.maxwait") != null) {
            dataSource.setMaxWaitMillis(Integer.valueOf((String) config.get("oracle.datasource.maxwait")) * 1000);
        }
        if (null != classLoader) {
            dataSource.setDriverClassLoader(classLoader);
        }
        maxLimit = Integer.valueOf((String) config.get("oracle.datasource.max-limit"));
        jdbcTemplate = new JdbcTemplate(dataSource);
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String statement) {
        return jdbcTemplate.queryForList(statement);
    }

    @Override
    public DriverMetaData get(DriverModel dr*iverModel) {
        DriverDataModel model = (DriverDataModel) driverModel;
        Map<String, Object> nameValueMap = (Map<String, Object>) model.getRequestData();

        String executeSql = null;
        try {
            executeSql = SqlTemplateUtil.parse(driverModel.getExpression(), (Map<String, Object>) nameValueMap.get("filters"));
        } catch (Exception e) {
            throw new DriverExecutorException("SQL 解析异常", e);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(SqlParser.limitSql("Oracle", executeSql, maxLimit));

        List<Map<String, Object>> fieldsResult = SqlTemplateUtil.removeField(result,
                (Map<String, Object>) nameValueMap.get("fields"));
        return new DriverMetaData(MetaDataTypeEnum.DATA, fieldsResult);
    }

    @Override
    public DriverMetaData put(DriverModel driverModel) {
        return null;
    }

    @Override
    public boolean valid() {

        return this.get("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual").size() > 0;
    }

    @Override
    public void close() {
        try {
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
