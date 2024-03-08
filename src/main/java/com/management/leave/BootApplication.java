package com.management.leave;

import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author zh
 */
@ServletComponentScan
@Aspect
@MapperScan("com.management.leave.db.mapper")
@SpringBootApplication(scanBasePackages = {"com.management.leave"})
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
