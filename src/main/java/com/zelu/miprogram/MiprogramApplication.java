package com.zelu.miprogram;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
@MapperScan(basePackages = {"com.zelu.miprogram.dao"})
public class MiprogramApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiprogramApplication.class, args);
    }

}
