package com.bbo.gmall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class GmallRedissonTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallRedissonTestApplication.class, args);
	}

}
