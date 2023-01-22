package it.simonericci97.github.meterpolis.meterpolis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    @Value("${spring.datasource.url}")
    private String jobDBurl;
    @Value("${spring.datasource.username}")
    private String jobDBuser;
    @Value("${spring.datasource.password}")
    private String jobDBpasswd;
    @Value("${spring.datasource.driver-class-name}")
    private String jobDbdriver;

    @Bean
    @Primary
    public DataSource jobRepositoryDataSource()  {

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jobDbdriver);
        dataSource.setUrl(jobDBurl);
        dataSource.setUsername(jobDBuser);
        dataSource.setPassword(jobDBpasswd);

        return dataSource;
    }
}
