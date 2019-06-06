package com.swjtu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.swjtu.mapper")
@MapperScan("com.swjtu.mapper")
public class TestDemoJarApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestDemoJarApplication.class, args);
	}

}
