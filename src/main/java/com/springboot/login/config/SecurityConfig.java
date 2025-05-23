package com.springboot.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	                "/admin/signup",
	                "/manager/login",
	                "/admin/create-manager",
	                "/customer/all",
	                "/admin/all",
	                "/manager/all",
	                "/admin/create-manager",
	                "/manager/hotel/add",
	                "/manager/hotel/view",
	                "/manager/hotel/update/by-id/**",
	                "/manager/hotel/update/by-name",
	                "/manager/hotel/delete/by-id/**",
	                "/manager/hotel/delete/by-name",
	                "/manager/hotel/all",
	                "/manager/room/add",
	                "/manager/room/view",
	                "/manager/room/update/**",
	                "/manager/room/delete/**",
	                "/customer/hotels",
	                "/customer/hotels/search",
	                "/customer/hotels/rooms",
	                "/customer/hotels/rooms/filter"
	            ).permitAll()
	            .anyRequest().authenticated()
	        );
        return http.build();
    }	
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
