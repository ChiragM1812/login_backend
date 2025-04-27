package com.springboot.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/customer/signup",
	                "/customer/login",
	                "/admin/login",
	                "/manager/login",
	                "/admin/signup",
	                "/manager/signup",
	                "/admin/create-manager",
	                "/customer/all"
	            ).permitAll()
	            .anyRequest().authenticated()
	        );
        return http.build();
    }	
}
