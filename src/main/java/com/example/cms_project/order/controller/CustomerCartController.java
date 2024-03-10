package com.example.cms_project.order.controller;

import com.example.cms_project.order.application.CartApplication;
import com.example.cms_project.order.domain.product.AddProductCartForm;
import com.example.cms_project.order.domain.redis.Cart;
import com.example.cms_project.user.client.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {
  private final CartApplication cartApplication;
  private final JwtAuthenticationProvider provider;

  @PostMapping
  public ResponseEntity<Cart> addCart(
      @RequestHeader("X-AUTH-TOKEN") String token,
      @RequestBody AddProductCartForm form) {
    return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(), form));
  }

  @GetMapping
  public ResponseEntity<Cart> showCart(@RequestHeader("X-AUTH-TOKEN") String token) {
    return ResponseEntity.ok(cartApplication.getCart(provider.getUserVo(token).getId()));
  }
}
