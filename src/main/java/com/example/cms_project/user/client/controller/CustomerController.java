package com.example.cms_project.user.client.controller;

import static com.example.cms_project.user.client.exception.ErrorCode.NOT_FOUND_USER;

import com.example.cms_project.user.client.domain.CustomerDto;
import com.example.cms_project.user.client.domain.UserVo;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.model.Customer;
import com.example.cms_project.user.client.security.JwtAuthenticationProvider;
import com.example.cms_project.user.client.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final JwtAuthenticationProvider provider;
  private final CustomerService customerService;
  @GetMapping("/gerInfo")
  public ResponseEntity<CustomerDto> getInfo(@RequestHeader("X-AUTH-TOKEN") String token) {
    UserVo vo = provider.getUserVo(token);
    Customer c = customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    return ResponseEntity.ok(CustomerDto.from(c));
  }
}
