package com.bin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bin.mapper")
public class IntegralApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegralApplication.class,args);
        System.out.println("启动成功！");
    }
}
