package com.example.cms_project.user.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

@Configuration
public class FeignConfig {

  @Value("${mailgun.api.key}")
  private String mailgunApiKey;

  @Qualifier(value = "mailgun")
  @Bean
  public BasicAuthenticationInterceptor basicAuthenticationInterceptor() {
    return new BasicAuthenticationInterceptor("api", mailgunApiKey);
  }
}
