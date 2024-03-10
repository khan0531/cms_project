package com.example.cms_project.order.controller;

import com.example.cms_project.order.domain.product.AddProductForm;
import com.example.cms_project.order.domain.product.AddProductItemForm;
import com.example.cms_project.order.domain.product.ProductDto;
import com.example.cms_project.order.service.ProductService;
import com.example.cms_project.user.client.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

  private final ProductService productService;
  private final JwtAuthenticationProvider provider;

  @PostMapping
  public ResponseEntity<ProductDto> addProduct(
      @RequestHeader(name = "X-AUTH-TOKEN") String token,
      @RequestBody AddProductForm form
      ) {
    return ResponseEntity.ok(ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(), form)));
  }


}
