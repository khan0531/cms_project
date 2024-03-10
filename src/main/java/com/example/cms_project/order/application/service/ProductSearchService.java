package com.example.cms_project.order.application.service;

import com.example.cms_project.common.exception.CustomException;
import com.example.cms_project.common.exception.ErrorCode;
import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
  private final ProductRepository productRepository;

  public List<Product> searchByName(String name) {
    return null;
  }

  public Product getProductId(Long productId) {
    return productRepository.findWithProductItemsById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
  }

  public List<Product> getListByProductIds(List<Long> productIds) {
    return productRepository.findAllById(productIds);
  }
}
