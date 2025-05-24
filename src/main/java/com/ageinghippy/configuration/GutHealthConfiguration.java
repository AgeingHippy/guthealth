package com.ageinghippy.configuration;

import com.ageinghippy.model.DTOMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class GutHealthConfiguration {

    @Bean
    public DTOMapper dtoMapper() {
        return new DTOMapper();
    }

    @Bean
    public jakarta.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}
