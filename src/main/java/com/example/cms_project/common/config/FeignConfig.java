package com.example.cms_project.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

@Configuration
public class FeignConfig {

  @Value("${mailgun.api.key}")
  private String MAILGUN_API_KEY;

  @Qualifier(value = "mailgun")
  @Bean
  public BasicAuthenticationInterceptor basicAuthenticationInterceptor() {
    return new BasicAuthenticationInterceptor("api", MAILGUN_API_KEY);
  }
}
