package com.example.cms_project.order.service;

import com.example.cms_project.order.domain.product.AddProductForm;
import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.repository.ProductRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public Product addProduct(Long sellerId, AddProductForm form) {
    return productRepository.save(Product.of(sellerId, form));
  }
}
