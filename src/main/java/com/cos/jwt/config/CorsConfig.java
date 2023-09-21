package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() { // 필터에 등록해줘야한다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        // config.setAllowCredentials(true); true로 설정하면 ajax,fetch 등 에서 요청하면 그 응답을 자바스크립트가 받을 수 있게 할건지 안할건지
        // 내 서버가 결정하는데 자바스크립트가 요청을 할 때 응답이 오지않는다.
        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용하겠다
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠다
        config.addAllowedMethod("*"); // 모든 post,get,delete,patch,put 요청을 허용하겠다
        source.registerCorsConfiguration("/api/**",config);
        return new CorsFilter(source);
    }
}
