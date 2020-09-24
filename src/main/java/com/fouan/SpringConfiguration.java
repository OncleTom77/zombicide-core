package com.fouan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@ComponentScan("com.fouan")
public class SpringConfiguration {

    @Bean
    public Random random() {
        return new Random();
    }
}
