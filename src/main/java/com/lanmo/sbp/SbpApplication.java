package com.lanmo.sbp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.lanmo.sbp.dao")
public class SbpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbpApplication.class, args);
    }

}
