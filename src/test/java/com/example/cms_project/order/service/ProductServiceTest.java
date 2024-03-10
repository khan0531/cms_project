package com.example.cms_project.order.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.cms_project.order.domain.product.AddProductForm;
import com.example.cms_project.order.domain.product.AddProductItemForm;
import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.repository.ProductItemRepository;
import com.example.cms_project.order.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void addProduct() {
    // given
    Long sellerId = 1L;
    AddProductForm form = makeAddProductForm("나이키 신발", "신발", 3);
    // when
    Product product = productService.addProduct(sellerId, form);
    Product result = productRepository.findWithProductItemsById(product.getId()).get();
    // then
    assertNotNull(result);
    assertEquals(result.getName(),"나이키 신발");
    assertEquals(result.getDescription(), "신발");
    assertEquals(result.getProductItems().size(), 3);
    assertEquals(result.getProductItems().get(0).getName(), "나이키 신발0");
    assertEquals(result.getProductItems().get(0).getPrice(), 10000);
    assertEquals(result.getProductItems().get(0).getCount(), 1);

    assertEquals(result.getProductItems().get(1).getName(), "나이키 신발1");
    assertEquals(result.getProductItems().get(2).getName(), "나이키 신발2");
  }

  private static final AddProductForm makeAddProductForm(String name, String description, int itemCount) {
    List<AddProductItemForm> itemForms = new ArrayList<>();
    for (int i = 0; i < itemCount; i++) {
      itemForms.add(makeAddProductItemForm(null, name + i));
    }

    return AddProductForm.builder()
        .name(name)
        .description(description)
        .items(itemForms)
        .build();
  }

  private static final AddProductItemForm makeAddProductItemForm(Long productId, String name) {
    return AddProductItemForm.builder()
        .productId(productId)
        .name(name)
        .price(10000)
        .count(1)
        .build();
  }
}