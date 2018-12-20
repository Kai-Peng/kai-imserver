package im.kai.server.service.user.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Configuration
@AutoConfigureAfter(MasterDB.class)
//指定实体映射扫描目录  required a single bean, but 2 were found
@MapperScan(basePackages = "im.kai.server.service.user.mapper", sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterDBConfiguration {

    //配置数据源
    @Primary
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource (MasterDB masterDB )throws SQLException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        log.info("MasterDB : " +mapper.writeValueAsString(masterDB) );
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(masterDB.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(masterDB.getPassword());
        mysqlXaDataSource.setUser(masterDB.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("masterDataSource");

        xaDataSource.setMinPoolSize(masterDB.getMinPoolSize());
        xaDataSource.setMaxPoolSize(masterDB.getMaxPoolSize());
        xaDataSource.setMaxLifetime(masterDB.getMaxLifetime());
        xaDataSource.setBorrowConnectionTimeout(masterDB.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(masterDB.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(masterDB.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(masterDB.getMaxIdleTime());
        xaDataSource.setTestQuery(masterDB.getTestQuery());
        return xaDataSource;
    }

    /**
     * 这里要指定mapper文件所在位置
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory (@Qualifier("masterDataSource") DataSource dataSource)
        throws Exception {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //bean.setMapperLocations(resolver.getResources("classpath*:im.kai.server.service.user.dao/*.xml"));
        bean.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/*.xml"));
        return bean.getObject();

    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager (@Qualifier("masterDataSource") DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate masterSqlSessionTemplate (@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
        throws Exception {

        return new SqlSessionTemplate(sqlSessionFactory);

    }



}