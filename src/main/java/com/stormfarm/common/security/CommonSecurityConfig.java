package com.stormfarm.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonSecurityConfig {

    @Value("${jwt.private-key:#{null}}")
    private String privateKey;

    @Value("${jwt.public-key:#{null}}")
    private String publicKey;

    @Bean
    public JwtService jwtService() {
        return new JwtService(privateKey, publicKey);
    }
}
