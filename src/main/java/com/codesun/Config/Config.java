package com.codesun.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Configuration
public class Config {

    @Bean
    public BASE64Encoder base64Encoder(){
        return new BASE64Encoder();
    }
    @Bean
    public BASE64Decoder base64Decoder(){
        return new BASE64Decoder();
    }
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }
}
