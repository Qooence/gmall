package com.bbo.gmall.search;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class GmallSearchWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallSearchWebApplication.class, args);
    }

}
