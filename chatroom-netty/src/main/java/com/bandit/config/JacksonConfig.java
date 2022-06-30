package com.bandit.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
@Slf4j
public class JacksonConfig {
    @Resource
    private MappingJackson2HttpMessageConverter converter;

    @PostConstruct
    void converter() {
        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance);
        converter.getObjectMapper().registerModule(simpleModule);
    }
}
