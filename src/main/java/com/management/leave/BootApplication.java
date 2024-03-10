package com.management.leave;

import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zh
 */
@ServletComponentScan
@Aspect
@EnableScheduling
@MapperScan("com.management.leave.dao.mapper")
@SpringBootApplication(scanBasePackages = {"com.management.leave"})
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
