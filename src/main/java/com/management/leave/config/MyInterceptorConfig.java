package com.management.leave.config;

import com.management.leave.interceptor.AuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author zh
 */
@Configuration
@ComponentScan
public class MyInterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private AuthTokenInterceptor authTokenInterceptor;


    public MyInterceptorConfig() {
    }

    /**
     * when login skip token check
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录相关动作跳过token校验
        registry.addInterceptor(authTokenInterceptor).order(1).addPathPatterns("/**")
                .excludePathPatterns(
                        "/sys/auth/*",
                        "/doc.html#/*",
                        "/doc.html",
                        "/webjars/**",
                        "/img.icons/**",
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/error",
                        "/favicon.ico"
                        );
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }


}
