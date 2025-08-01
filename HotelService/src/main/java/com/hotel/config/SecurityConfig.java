package com.hotel.config;

import com.hotel.security.JwtAuthFilter;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Autowired
  private JwtAuthFilter jwtAuthFilter;    // your OncePerRequestFilter that extracts + validates JWT

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	  http
      // disable CSRF for stateless APIs
      .csrf(csrf -> csrf.disable())
      // disable default login form and HTTP Basic
      .httpBasic(basic -> basic.disable())
      .formLogin(form -> form.disable())
      // make sure sessions are not used
      .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      // define your authorization rules
      .authorizeHttpRequests(auth -> auth
        // if you have any public endpoints, list them here:
        //.requestMatchers("/public/**").permitAll()
        .anyRequest().authenticated()
      );

    // Register the JWT filter just before Spring’s UsernamePasswordAuthenticationFilter
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // Allow calls from your front end / API gateway
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
      }
    };
  }
}
