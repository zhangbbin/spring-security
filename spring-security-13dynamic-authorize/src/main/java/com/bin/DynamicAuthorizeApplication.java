package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DynamicAuthorizeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamicAuthorizeApplication.class,args);
        System.out.println("启动成功！");
    }
}
