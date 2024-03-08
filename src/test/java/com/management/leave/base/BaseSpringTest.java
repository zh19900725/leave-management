package com.management.leave.base;

import com.management.leave.BootApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试基础类
 * 1.默认指定dev配置,dev为本地配置，需要本地搭建数据库、kafka、redis
 * 2.只需继承即可进行单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSpringTest {

}
