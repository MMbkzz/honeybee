package com.stackstech.honeybee.server.core.conf;

import com.stackstech.honeybee.server.core.enums.types.*;
import com.stackstech.honeybee.server.core.handler.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.stackstech.honeybee.server.*.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceConfig {

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "app.datasource.db")
    public DataSource dbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jsonTypeHandler")
    public JsonTypeHandler newJsonTypeHandler() {
        return new JsonTypeHandler();
    }


    @Bean
    public AssetsEnumTypeHandler newAssetsEnumTypeHandler() {
        return new AssetsEnumTypeHandler(AssetsCatalogType.class);
    }

    @Bean
    public AuditEnumTypeHandler newAuditEnumTypeHandler() {
        return new AuditEnumTypeHandler(AuditOperationType.class);
    }

    @Bean
    public DBEnumTypeHandler newDBEnumTypeHandler() {
        return new DBEnumTypeHandler(DataSourceType.class);
    }

    @Bean
    public MessageEnumTypeHandler newMessageEnumTypeHandler() {
        return new MessageEnumTypeHandler(MessageType.class);
    }

    @Bean
    public RuleEnumTypeHandler newRuleEnumTypeHandler() {
        return new RuleEnumTypeHandler(QualityRuleType.class);
    }

    @Bean
    public StatusEnumTypeHandler newStatusEnumTypeHandler() {
        return new StatusEnumTypeHandler(EntityStatusType.class);
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/stackstech/honeybee/server/dao/mapper/*.xml"));
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));
        bean.setDefaultEnumTypeHandler(BaseEnumTypeHandler.class);
        bean.setTypeHandlers(
                newJsonTypeHandler(),
                newAssetsEnumTypeHandler(),
                newAuditEnumTypeHandler(),
                newDBEnumTypeHandler(),
                newMessageEnumTypeHandler(),
                newRuleEnumTypeHandler(),
                newStatusEnumTypeHandler()
        );
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
