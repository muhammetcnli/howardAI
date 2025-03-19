package com.atlas.howardAI.configuration;

import com.atlas.howardAI.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    public SecurityConfig(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests(registry ->{
                        registry.requestMatchers("/").permitAll();
                        registry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 ->
                    oauth2.userInfoEndpoint(userInfo ->
                        userInfo.userService(oAuth2UserService)
                    )
                )
                .formLogin(Customizer.withDefaults());

        return http.build();


    }
}
