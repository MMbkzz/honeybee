package com.stackstech.honeybee.connector.mysql;

import com.stackstech.dcp.connector.core.AbstractBasicDataSourceSession;
import com.stackstech.dcp.connector.core.ResourceSession;
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

public class MysqlSession extends AbstractBasicDataSourceSession implements ResourceSession {

    private JdbcTemplate jdbcTemplate;

    private MysqlSession(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static MysqlSession getInstance(ClassLoader classLoader) {
        return new MysqlSession(classLoader);
    }

    /**
     * mysql.datasource.driver-class-name=com.mysql.jdbc.Driver
     * mysql.datasource.url=jdbc:mysql://localhost:3306/db?charset=UTF8&useSSL=false&useUnicode=true&characterEncoding=utf8
     * mysql.datasource.username=root
     * mysql.datasource.password=admin
     * mysql.datasource.max-idle=10
     * mysql.datasource.max-wait=10000
     * mysql.datasource.min-idle=5
     * mysql.datasource.initial-size=5
     * mysql.datasource.validation-query=SELECT 1
     * mysql.datasource.test-on-borrow=false
     * mysql.datasource.test-while-idle=true
     * mysql.datasource.time-between-eviction-runs-millis=18800
     */
    public MysqlSession initialize(Map<String, Object> config) {
        dataSource = new BasicDataSource();
        dataSource.setUrl((String) config.get("mysql.datasource.url"));
        dataSource.setUsername((String) config.get("mysql.datasource.username"));
        dataSource.setPassword((String) config.get("mysql.datasource.password"));
        dataSource.setDriverClassName((String) config.get("mysql.datasource.driver-class-name"));
        if (config.get("mysql.datasource.query-timeout") != null) {
            dataSource.setDefaultQueryTimeout(Integer.valueOf((String) config.get("mysql.datasource.query-timeout")));
        }

        if (config.get("mysql.datasource.maxwait") != null) {
            dataSource.setMaxWaitMillis(Integer.parseInt((String) config.get("mysql.datasource.maxwait")) * 1000);
        }

        //dataSource.setMaxTotal(2);
        dataSource.setMaxWaitMillis(2000);
        if (null != classLoader) {
            dataSource.setDriverClassLoader(classLoader);
        }

        maxLimit = Integer.parseInt((String) config.get("mysql.datasource.max-limit"));
        jdbcTemplate = new JdbcTemplate(dataSource);
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String statement) {
        return jdbcTemplate.queryForList(statement);
    }

    @Override
    public DriverMetaData get(DriverModel driverModel) {
        //DriverDataModel model = (DriverDataModel) driverModel;

        Map<String, Object> nameValueMap = (Map<String, Object>) driverModel.getRequestData();

        String executeSql = null;
        try {
            executeSql = SqlTemplateUtil.parse(driverModel.getExpression(), (Map<String, Object>) nameValueMap.get("filters"));
        } catch (Exception e) {
            throw new DriverExecutorException("SQL 解析异常", e);
        }

        List<Map<String, Object>> result = jdbcTemplate.queryForList(SqlParser.limitSql("Mysql", executeSql, maxLimit));
//        if (result.size() > maxLimit) {
//           result = result.subList(0, maxLimit);
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
        return this.get("select 1=1").size() > 0;
    }

    @Override
    public void close() {
        try {
            if (dataSource != null) {
                dataSource.close();
                dataSource = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
