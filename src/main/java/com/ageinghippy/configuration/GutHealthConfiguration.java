package com.ageinghippy.configuration;

import com.ageinghippy.model.DTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class GutHealthConfiguration {

    @Bean
    public DTOMapper dtoMapper() {
        return new DTOMapper();
    }

    @Bean
    public jakarta.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

}
