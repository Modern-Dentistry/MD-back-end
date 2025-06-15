package com.rustam.modern_dentistry.config;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.util.JwtAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthUtil jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x -> {
                    x.requestMatchers(getPublicEndpoints()).permitAll();
                    registerModulePermissions(x);
                    x.requestMatchers(getUserRoleEndpoints()).hasAuthority(Role.USER.getValue());
                    x.requestMatchers(getAdminRoleEndpoints()).hasAuthority(Role.ADMIN.getValue());
                    x.requestMatchers("/**").hasAuthority("SUPER_ADMIN");
                    x.anyRequest().authenticated();
                })
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(LogoutConfigurer::permitAll)
                .httpBasic(withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(cors -> cors.configurationSource(request -> getCorsConfiguration()))
                .build();
    }

    private void registerModulePermissions(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry x) {
        List<String> modules = List.of("patient", "doctor", "appointment","add-worker","general-calendar","patient-blacklist",
                "reservation","technician","workers-work-schedule"

        );

        for (String module : modules) {
            String basePath = "/api/v1/" + module + "/**";
            x.requestMatchers(HttpMethod.GET, basePath).hasAuthority(basePath + ":READ");
            x.requestMatchers(HttpMethod.POST, basePath).hasAuthority(basePath + ":CREATE");
            x.requestMatchers(HttpMethod.PUT, basePath).hasAuthority(basePath + ":UPDATE");
            x.requestMatchers(HttpMethod.DELETE, basePath).hasAuthority(basePath + ":DELETE");
        }
    }

    private String[] getPublicEndpoints() {
        return new String[]{
                "/api/v1/auth/login",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html/**"
        };
    }

    private String[] getUserRoleEndpoints() {
        return new String[]{};
    }

    private String[] getAdminRoleEndpoints() {
        return new String[]{
                "/api/v1/add-worker/create"
        };
    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        return corsConfiguration;
    }
}

