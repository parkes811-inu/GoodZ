package com.springproject.goodz.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests.antMatchers("/**").permitAll()
                                                    .antMatchers("/css/**", "/js/**", "/img/**").permitAll()    
                                                    .anyRequest().permitAll()
                                                    // .anyRequest().authenticated()
                                            );
        return http.build();
    }
    
}
