package com.example.cms_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ServletComponentScan
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class CmsProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(CmsProjectApplication.class, args);
  }

}
