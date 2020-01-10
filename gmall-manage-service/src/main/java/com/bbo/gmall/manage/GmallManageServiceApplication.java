package com.bbo.gmall.manage;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.bbo.gmall.manage.mapper")
public class GmallManageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GmallManageServiceApplication.class, args);
	}
}
