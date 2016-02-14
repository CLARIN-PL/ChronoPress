package pl.edu.pwr.configuration.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
public class DataSourceConfig {
 
   @Value("${spring.datasource.username}")
   private String user;
 
   @Value("${spring.datasource.password}")
   private String password;
 
   @Value("${spring.datasource.url}")
   private String dataSourceUrl;
 
   @Value("${spring.datasource.dataSourceClassName}")
   private String dataSourceClassName;

   @Bean
   public DataSource primaryDataSource() {
       Properties dsProps = new Properties();
       dsProps.put("url", dataSourceUrl);
       dsProps.put("user", user);
       dsProps.put("password", password);

       Properties configProps = new Properties();
          configProps.put("dataSourceClassName", dataSourceClassName);
          configProps.put("poolName", "SpringBootHikariCP");
          configProps.put("maximumPoolSize", 5);
          configProps.put("minimumIdle", 3);
          configProps.put("maxLifetime", 2000000);
          configProps.put("connectionTimeout", 30000);
          configProps.put("idleTimeout", 30000);
          configProps.put("dataSourceProperties", dsProps);
 
      HikariConfig hc = new HikariConfig(configProps);
      return new HikariDataSource(hc);
   }
}