package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurity01Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity01Application.class,args);
        System.out.println("正常启动！");
    }
}
