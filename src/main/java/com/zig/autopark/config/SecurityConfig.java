package com.zig.autopark.config;

import com.zig.autopark.security.JwtAuthenticationFilter;
import com.zig.autopark.security.TimeZoneOffsetFilter;
import com.zig.autopark.service.MultiUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MultiUserDetailsService multiUserDetailsService;
    private final TimeZoneOffsetFilter timeZoneOffsetFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth").permitAll()
                        .requestMatchers("/get").permitAll()
                        .requestMatchers("/get/geo").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers("/auth");
                    csrf.ignoringRequestMatchers("*/rest/**");
                    csrf.ignoringRequestMatchers("/get");
                    csrf.ignoringRequestMatchers("/get/geo");
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
//                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .userDetailsService(multiUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(timeZoneOffsetFilter, JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(multiUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
