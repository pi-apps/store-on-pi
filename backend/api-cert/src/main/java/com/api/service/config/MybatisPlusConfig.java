
package com.api.service.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * mybatis-plus配置
 */
@Configuration
public class MybatisPlusConfig {
    @Value("${spring.datasource.druid.url}")
    private String url;

    @Value("${spring.datasource.druid.username}")
    private String userName;

    @Value("${spring.datasource.druid.password}")
    private String passWord;

    @Value("${spring.datasource.druid.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.druid.initialSize}")
    private Integer initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private Integer minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private Integer maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private Integer maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private Boolean testOnReturn;

    @Value("${spring.datasource.druid.poolPreparedStatements}")
    private Boolean poolPreparedStatements;

    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    private String filters = "stat,wall";

    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=1000";


    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dateSource() throws SQLException {
        DruidDataSource dataSourceBean = new DruidDataSource();
        dataSourceBean.setUrl(url);
        dataSourceBean.setUsername(userName);
        dataSourceBean.setPassword(passWord);
        dataSourceBean.setDriverClassName(driverClassName);
        dataSourceBean.setInitialSize(initialSize);
        dataSourceBean.setMinIdle(minIdle);
        dataSourceBean.setMaxActive(maxActive);
        dataSourceBean.setMaxWait(maxWait);
        dataSourceBean.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSourceBean.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSourceBean.setValidationQuery(validationQuery);
        dataSourceBean.setTestWhileIdle(testWhileIdle);
        dataSourceBean.setTestOnBorrow(testOnBorrow);
        dataSourceBean.setTestOnReturn(testOnReturn);
        dataSourceBean.setPoolPreparedStatements(poolPreparedStatements);
        dataSourceBean.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        dataSourceBean.setConnectionProperties(connectionProperties);
        dataSourceBean.setFilters(filters);
        return dataSourceBean;
    }

}
