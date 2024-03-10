package com.example.cms_project.order.service;

import static com.example.cms_project.common.exception.ErrorCode.NOT_FOUND_ITEM;
import static com.example.cms_project.common.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.example.cms_project.common.exception.CustomException;
import com.example.cms_project.order.domain.product.AddProductItemForm;
import com.example.cms_project.order.domain.product.UpdateProductItemForm;
import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.entity.ProductItem;
import com.example.cms_project.order.repository.ProductItemRepository;
import com.example.cms_project.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.cms_project.common.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductItemService {

  private final ProductRepository productRepository;
  private final ProductItemRepository productItemRepository;

  @Transactional
  public Product addProductItem(Long sellerId, AddProductItemForm form) {
    Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
        .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

    if (product.getProductItems().stream()
        .anyMatch(item -> item.getName().equals(form.getName()))) {
      throw new CustomException(ErrorCode.SAME_ITEM_NAME);
    }

    ProductItem productItem = ProductItem.of(sellerId, form);
    product.getProductItems().add(productItem);

    return product;
  }

  @Transactional
  public ProductItem updateProductItem(Long sellerId, UpdateProductItemForm form) {
    ProductItem productItem = productItemRepository.findById(form.getId())
        .filter(pi -> pi.getSellerId().equals(sellerId)).orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

    productItem.setName(form.getName());
    productItem.setPrice(form.getPrice());
    productItem.setCount(form.getCount());
    return productItem;
  }
}
