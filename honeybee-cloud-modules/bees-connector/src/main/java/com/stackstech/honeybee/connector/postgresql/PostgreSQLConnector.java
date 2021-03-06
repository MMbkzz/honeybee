package com.stackstech.honeybee.connector.postgresql;

import com.stackstech.honeybee.connector.core.util.SqlParser;
import com.stackstech.honeybee.connector.core.util.SqlTemplateUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PostgreSQLConnector extends AbstractBasicDataSourceConnector implements ResourceSession {

    private JdbcTemplate jdbcTemplate;

    private PostgreSQLConnector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static PostgreSQLConnector getInstance(ClassLoader classLoader) {
        return new PostgreSQLConnector(classLoader);
    }

    public PostgreSQLConnector initialize(Map<String, Object> config) {
        dataSource = new BasicDataSource();
        dataSource.setUrl((String) config.get("postgresql.datasource.url"));
        dataSource.setUsername((String) config.get("postgresql.datasource.username"));
        dataSource.setPassword((String) config.get("postgresql.datasource.password"));
        dataSource.setDriverClassName((String) config.get("postgresql.datasource.driver-class-name"));
        if (config.get("postgresql.datasource.maxwait") != null) {
            dataSource.setMaxWaitMillis(Integer.valueOf((String) config.get("postgresql.datasource.maxwait")) * 1000);
        }
        if (config.get("postgresql.datasource.query-timeout") != null) {
            dataSource.setDefaultQueryTimeout(Integer.valueOf((String) config.get("postgresql.datasource.query-timeout")));
        }
        if (null != classLoader) {
            dataSource.setDriverClassLoader(classLoader);
        }
        maxLimit = Integer.valueOf((String) config.get("postgresql.datasource.max-limit"));
        jdbcTemplate = new JdbcTemplate(dataSource);
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String statement) {
        return jdbcTemplate.queryForList(statement);
    }

    @Override
    public DriverMetaData get(DriverModel driverModel) {
        DriverDataModel model = (DriverDataModel) driverModel;

        Map<String, Object> nameValueMap = (Map<String, Object>) model.getRequestData();

        String executeSql = null;
        try {
            executeSql = SqlTemplateUtil.parse(driverModel.getExpression(), (Map<String, Object>) nameValueMap.get("filters"));
        } catch (Exception e) {
            throw new DriverExecutorException("SQL ????????????:", e);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(SqlParser.limitSql("PostgreSQL", executeSql, maxLimit));

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
        return this.get("select now()").size() > 0;
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
