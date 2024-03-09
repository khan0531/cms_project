package com.example.cms_project.user.client.application;

import static com.example.cms_project.user.client.exception.ErrorCode.LOGIN_CHECK_FAIL;

import com.example.cms_project.user.client.domain.SignInForm;
import com.example.cms_project.user.client.domain.type.UserType;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.model.Customer;
import com.example.cms_project.user.client.security.JwtAuthenticationProvider;
import com.example.cms_project.user.client.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SignInApplication {

  private final CustomerService customerService;
  private final JwtAuthenticationProvider provider;

  public String customerLoginToken(SignInForm form) {
    // 1. 로그인 가능 여부
    Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));
    // 2. 토큰을 발행
    // 3. 토큰을 response
    return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
  }
}
