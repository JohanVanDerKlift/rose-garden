package nl.johanvanderklift.roseGarden.config;

import nl.johanvanderklift.roseGarden.filter.JwtRequestFilter;
import nl.johanvanderklift.roseGarden.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomPasswordEncoder encoder;

    public SecurityConfig(CustomUserDetailService customUserDetailService, JwtRequestFilter jwtRequestFilter, CustomPasswordEncoder encoder) {
        this.customUserDetailService = customUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.encoder = encoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailService)
                .passwordEncoder(encoder.passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                // requestMatchers for user and authentication controller
                .requestMatchers(HttpMethod.POST, "/authenticate", "/user").permitAll()
                .requestMatchers("/user/auth", "/user/admin/{username}").hasRole("ADMIN")
                .requestMatchers("/user/address", "/user/address/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/user").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/user").authenticated()
                .requestMatchers(HttpMethod.GET, "/user", "/user/search").hasRole("ADMIN")
                .requestMatchers("/authenticated").authenticated()
                // RequestMatchers for product controller
                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/product/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/product").permitAll()
                .requestMatchers(HttpMethod.POST, "/product").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/product/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/product/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/product/{id}").hasRole("ADMIN")
                // RequestMatchers for weborder controller
                .requestMatchers("/weborder").authenticated()
                .requestMatchers("/weborder/{webOderId}").authenticated()
                .requestMatchers("weborder/{webOrderId}/address/{addressId}").authenticated()
                .requestMatchers("/weborder/admin").hasRole("ADMIN")
                .requestMatchers("/weborder/admin/{webOrderId}").hasRole("ADMIN")
                // RequestMatchers for file controller
                .requestMatchers("/upload/pdf/**").authenticated()
                .requestMatchers("/download/*").authenticated()
                .requestMatchers("/file/delete/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

}

