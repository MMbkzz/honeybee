package com.stackstech.honeybee.connector.hive;

import com.alibaba.druid.pool.DruidDataSource;
import com.stackstech.honeybee.connector.core.util.SqlParser;
import com.stackstech.honeybee.connector.core.util.SqlTemplateUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HiveConnector extends AbstractDruidDataSourceConnector implements ResourceSession {

    private static final Logger logger = LoggerFactory.getLogger(HiveConnector.class);

    private JdbcTemplate jdbcTemplate;

    private HiveConnector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static HiveConnector getInstance(ClassLoader classLoader) {
        return new HiveConnector(classLoader);
    }

    public HiveConnector initialize(Map<String, Object> config) {
        dataSource = new DruidDataSource();
        dataSource.setUrl((String) config.get("hive.datasource.url"));
        dataSource.setUsername((String) config.get("hive.datasource.username"));
        dataSource.setPassword((String) config.get("hive.datasource.password"));
        dataSource.setDriverClassName((String) config.get("hive.datasource.driver-class-name"));
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(180);

        if (config.get("hive.datasource.query-timeout") != null) {
            dataSource.setValidationQueryTimeout(Integer.parseInt((String) config.get("hive.datasource.query-timeout")));
        }
        if (config.get("hive.datasource.maxwait") != null) {
            dataSource.setMaxWait(Integer.parseInt((String) config.get("hive.datasource.maxwait")) * 1000);
        }
        if (null != classLoader) {
            dataSource.setDriverClassLoader(classLoader);
        }
        maxLimit = Integer.parseInt((String) config.get("hive.datasource.max-limit"));

        logger.info("HiveSession????????????.....");

        //??????kerbos??????
        if (config.get("hive.security.authentication") != null) {
            org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
            conf.set("hadoop.security.authentication", (String) config.get("hive.security.authentication"));
            try {
                UserGroupInformation.setConfiguration(conf);
                UserGroupInformation.loginUserFromKeytab((String) config.get("hive.security.kerberos.principal"),
                        (String) config.get("hive.security.keytab"));
            } catch (IOException e) {
                logger.error("Hive??????kerbors????????????", e);
            }
        }
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
            throw new DriverExecutorException("SQL ???????????????", e);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(SqlParser.limitSql("Hive", executeSql, maxLimit));

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
        dataSource.close();
    }
}
