package com.dp.homework.films.config;


import com.dp.homework.films.service.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

import static com.dp.homework.films.constants.SecurityConstants.*;
import static com.dp.homework.films.constants.UserRolesConstants.ADMIN;
import static com.dp.homework.films.constants.UserRolesConstants.MANAGER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;


    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                //Настройка http запросов - кому куда можно/нельзя
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                                .requestMatchers(FILMS_WHITE_LIST.toArray(String[]::new)).permitAll()
                                .requestMatchers(DIRECTORS_WHITE_LIST.toArray(String[]::new)).permitAll()
                                .requestMatchers(DIRECTORS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, MANAGER)
                                .requestMatchers(USERS_WHITE_LIST.toArray(String[]::new)).authenticated()
                                .requestMatchers(FILMS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, MANAGER)
                                .requestMatchers(USERS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, MANAGER)
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                );
        return http.build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

}

