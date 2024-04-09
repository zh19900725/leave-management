package com.management.leave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfigurer {
    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1、允许来源
        corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        // 2、允许任何请求头
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // 3、允许任何方法
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        // 4. 设置允许的返回头
        corsConfiguration.addExposedHeader("TraceId");
        // 4、允许凭证
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
         return new CorsFilter(source);

    }
}
