package com.hms.config;

import com.hms.exceptions.FilterExceptionHandler;
import com.hms.security.CustomUserDetailsService;
import com.hms.security.JwtAuthenticationEntryPoint;
import com.hms.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity(debug = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private FilterExceptionHandler filterExceptionHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("inside auth configure");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        log.info("leaving auth configure");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("inside http");
        http.cors().and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .antMatchers("/hms/login/")
                .permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterExceptionHandler, LogoutFilter.class);
        log.info("leaving http");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("inside password encoder");
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.info("inside auth manager bean");
        return super.authenticationManagerBean();
    }
}
