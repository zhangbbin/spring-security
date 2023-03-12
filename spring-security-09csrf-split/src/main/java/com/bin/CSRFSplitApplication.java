package com.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CSRFSplitApplication {
    public static void main(String[] args) {
        SpringApplication.run(CSRFSplitApplication.class,args);
        System.out.println("启动成功！");
    }
}
