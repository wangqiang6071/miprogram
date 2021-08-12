package com.zelu.miprogram.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.Properties;

/**
 * @author wangqiang
 * @Date: 2021/6/8 16:22
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
//@MapperScan("com.luze.dockerapi.dao")
public class MybatisPlusConfig implements MetaObjectHandler{

        //测试
        @Override
        public void insertFill(MetaObject metaObject) {
            // this.setFieldValByName("orderFormId","1266883",metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            //this.setFieldValByName("createTime",new Date(),metaObject);
        }

        //分页
        @Bean
        public PaginationInterceptor paginationInterceptor() {
            return new PaginationInterceptor();
        }


        //自动字段填充
        @Bean
        public ISqlInjector sqlInjector() {
            return new LogicSqlInjector();
        }

    /**
     * 打印 sql-格式化
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //格式化sql语句
        Properties properties = new Properties();
        properties.setProperty("format", "true");
        performanceInterceptor.setProperties(properties);
        return performanceInterceptor;
    }





//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Bean
//    public DruidDataSource dataSource(){
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(url);
//        druidDataSource.setDriverClassName(driverClassName);
//        druidDataSource.setUsername(username);
//        druidDataSource.setPassword(password);
//        return druidDataSource;
//    }
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource());
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setDefaultEnumTypeHandler(EnumOrdinalTypeHandler.class);
//        sqlSessionFactoryBean.setConfiguration(configuration);
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PaginationInterceptor()});
//        return sqlSessionFactoryBean.getObject();
//    }
//
//    @Bean
//    public DataSourceTransactionManager transactionManager(){
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
//        transactionManager.setDataSource(dataSource());
//        return transactionManager;
//    }
}
