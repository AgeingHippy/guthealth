package com.ageinghippy.configuration;

import com.ageinghippy.model.MyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GutHealthConfiguration {

    @Bean
    public MyMapper myMapper() {
        MyMapper myMapper = new MyMapper();
//        myMapper.registerModule(new RecordModule());
        return myMapper;
    }

}
