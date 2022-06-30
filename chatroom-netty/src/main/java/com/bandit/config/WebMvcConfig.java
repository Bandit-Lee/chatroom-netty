package com.bandit.config;

import com.bandit.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .addPathPatterns("/**")
        .excludePathPatterns("/api/user/login","/login","/chatroom","/");
    }

    // 后端允许跨域请求
    // 生产环境下，后端应隐藏请求接口，由前端通过部署时反向代理实现跨域请求。例如通过nginx服务器
//    @Bean
//    @Order(0)
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOriginPattern("*");
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        config.addExposedHeader("*");
//        config.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource configSource
//                = new UrlBasedCorsConfigurationSource();
//        configSource.registerCorsConfiguration("/api/**", config);
//        return new CorsFilter(configSource);
//    }
}
