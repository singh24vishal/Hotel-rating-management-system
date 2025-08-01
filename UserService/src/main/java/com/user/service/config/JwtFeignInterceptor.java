package com.user.service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

//@Component
public class JwtFeignInterceptor implements RequestInterceptor {
  @Override
  public void apply(RequestTemplate template) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof UsernamePasswordAuthenticationToken) {
      String token = ((UsernamePasswordAuthenticationToken) auth).getCredentials().toString();
      template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
  }
}
