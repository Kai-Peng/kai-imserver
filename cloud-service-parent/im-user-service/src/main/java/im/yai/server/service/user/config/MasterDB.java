package im.kai.server.service.user.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 主数据库配置信息
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 扫描配置中心的配置
 */
//@ConfigurationProperties(prefix = "mysql.datasource.test1")
public class MasterDB {

    @Value("${spring.datasource.url}")
    public String url;
    @Value("${spring.datasource.username}")
    public String username ;
    @Value("${spring.datasource.password}")
    public String password ;

    public int  minPoolSize = 3;
    public int  maxPoolSize = 25 ;
    public int  maxLifetime = 20000 ;
    public int  borrowConnectionTimeout = 30 ;
    public int  loginTimeout = 30;
    public int  maintenanceInterval = 60;
    public int  maxIdleTime = 60 ;
    public String testQuery = "select 1";


}
