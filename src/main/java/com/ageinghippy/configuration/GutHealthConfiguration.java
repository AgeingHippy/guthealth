package com.ageinghippy.configuration;

import com.ageinghippy.model.DTOMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GutHealthConfiguration {

    @Bean
    public DTOMapper myMapper() {
        DTOMapper DTOMapper = new DTOMapper();
//        myMapper.registerModule(new RecordModule());
        return DTOMapper;
    }

}
