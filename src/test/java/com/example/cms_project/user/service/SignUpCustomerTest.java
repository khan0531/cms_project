package com.example.cms_project.user.service;

import com.example.cms_project.user.client.domain.SignUpForm;
import com.example.cms_project.user.client.model.Customer;
import com.example.cms_project.user.client.service.SignUpCustomerService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class SignUpCustomerServiceTest {

  @Autowired
  private SignUpCustomerService service;

  @Test
  void signUp() {
    SignUpForm form = SignUpForm.builder()
        .name("name")
        .email("abc@gmail.com")
        .password("1234")
        .birth(LocalDate.now())
        .phone("010-1234-5678")
        .build();
    Customer customer = service.signUp(form);
    Assert.isTrue(customer.getId() != null, "id is not null");
  }
}
