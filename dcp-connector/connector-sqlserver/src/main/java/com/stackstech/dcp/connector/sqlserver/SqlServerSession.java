package com.stackstech.dcp.connector.sqlserver;


import com.stackstech.dcp.connector.core.AbstractBasicDataSourceSession;
import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.entity.DriverDataModel;
import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.entity.DriverModel;
import com.stackstech.dcp.connector.core.enums.MetaDataTypeEnum;
import com.stackstech.dcp.connector.core.exception.DriverExecutorException;
import com.stackstech.dcp.connector.core.util.SqlParser;
import com.stackstech.dcp.connector.core.util.SqlTemplateUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqlServerSession extends AbstractBasicDataSourceSession implements ResourceSession {

    private JdbcTemplate jdbcTemplate;

    private SqlServerSession(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static SqlServerSession getInstance(ClassLoader classLoader) {
        return new SqlServerSession(classLoader);
    }

    public SqlServerSession initialize(Map<String, Object> config) {
        dataSource = new BasicDataSource();
        dataSource.setUrl((String) config.get("sqlserver.datasource.url"));
        dataSource.setUsername((String) config.get("sqlserver.datasource.username"));
        dataSource.setPassword((String) config.get("sqlserver.datasource.password"));
        dataSource.setDriverClassName((String) config.get("sqlserver.datasource.driver-class-name"));
        if (config.get("sqlserver.datasource.query-timeout") != null) {
            dataSource.setDefaultQueryTimeout(Integer.valueOf((String) config.get("sqlserver.datasource.query-timeout")));
        }
        if (config.get("sqlserver.datasource.maxwait") != null) {
            dataSource.setMaxWaitMillis(Integer.valueOf((String) config.get("sqlserver.datasource.maxwait")) * 1000);
        }
        if (null != classLoader) {
            dataSource.setDriverClassLoader(classLoader);
        }
        maxLimit = Integer.valueOf((String) config.get("sqlserver.datasource.max-limit"));
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
            throw new DriverExecutorException("SQL 解析异常", e);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(SqlParser.limitSql("SqlServer", executeSql, maxLimit));
//        if (result.size() > maxLimit) {
//            result = result.subList(0, maxLimit);
//        }
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
        return this.get("select GETDATE()").size() > 0;
    }

    @Override
    public void close() {
        try {
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }

}
