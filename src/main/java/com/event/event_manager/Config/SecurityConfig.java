package com.event.event_manager.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // 誰でもアクセスできるURL
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/error/401",
                    "/css/**",
                    "/js/**"
                ).permitAll()

                // POST
                .requestMatchers(HttpMethod.POST, "/**").authenticated()

                // PUT
                .requestMatchers(HttpMethod.PUT, "/**").authenticated()

                // DELETE
                .requestMatchers(HttpMethod.DELETE, "/**").authenticated()

                // PATCH
                .requestMatchers(HttpMethod.PATCH, "/**").authenticated()

                // その他のアクセス
                .anyRequest().authenticated()
            )

            // 未ログイン時は401画面へ
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    request.getRequestDispatcher("/error/401")
                           .forward(request, response);
                })
            )

            .formLogin(form -> form.disable())

            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
