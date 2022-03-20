package ru.itmo.dolzhanskii.sd.hw11.configuration;

import java.time.Clock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

//    @Bean
//    @Profile("test")
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("sa");
//
//        return dataSource;
//    }

    @Bean
    public Clock clock() {
        return Mockito.mock(Clock.class);
    }
}
