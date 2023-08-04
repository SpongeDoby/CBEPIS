package com.example.cbepis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.cbepis.dao")
@EnableScheduling
public class CbepisApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbepisApplication.class, args);
    }

}
