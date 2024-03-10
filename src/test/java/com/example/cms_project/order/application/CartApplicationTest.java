package com.example.cms_project.order.application;

import static org.junit.jupiter.api.Assertions.*;

import com.example.cms_project.order.application.service.ProductService;
import com.example.cms_project.order.domain.product.AddProductCartForm;
import com.example.cms_project.order.domain.product.AddProductForm;
import com.example.cms_project.order.domain.product.AddProductItemForm;
import com.example.cms_project.order.domain.redis.Cart;
import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CartApplicationTest {

  @Autowired
  private CartApplication cartApplication;
  @Autowired
  private ProductService productService;
  @Autowired
  private ProductRepository productRepository;

  @Test
  void ADD_TEST_MODIFY() {

    Long customerId = 100L;

    cartApplication.clearCart(customerId);

    Product p = add_Product();
    Product result = productRepository.findWithProductItemsById(p.getId()).get();

    assertNotNull(result);

    assertEquals(result.getName(),"나이키 신발");
    assertEquals(result.getDescription(), "신발");

    assertEquals(result.getProductItems().size(), 3);
    assertEquals(result.getProductItems().get(0).getName(), "나이키 신발0");
    assertEquals(result.getProductItems().get(0).getPrice(), 10000);
    assertEquals(result.getProductItems().get(0).getCount(), 1);

    assertEquals(result.getProductItems().get(1).getName(), "나이키 신발1");
    assertEquals(result.getProductItems().get(2).getName(), "나이키 신발2");

    Cart cart = cartApplication.addCart(customerId, makeAddForm(result));

    assertEquals(cart.getMessages().size(), 0);

    cart = cartApplication.getCart(customerId);
    assertEquals(cart.getMessages().size(), 1);

  }

  AddProductCartForm makeAddForm(Product p) {
    AddProductCartForm.ProductItem productItem =
        AddProductCartForm.ProductItem.builder()
        .id(p.getProductItems().get(0).getId())
        .name(p.getProductItems().get(0).getName())
            .count(5)
            .price(20000)
            .build();
    return AddProductCartForm.builder()
            .id(p.getId())
            .name(p.getName())
            .sellerId(p.getSellerId())
            .description(p.getDescription())
            .items(List.of(productItem)).build();

  }
  private Product add_Product() {
    // given
    Long sellerId = 1L;
    AddProductForm form = makeAddProductForm("나이키 신발", "신발", 3);
    return productService.addProduct(sellerId, form);
  }

  private static AddProductForm makeAddProductForm(String name, String description, int itemCount) {
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

  private static AddProductItemForm makeAddProductItemForm(Long productId, String name) {
    return AddProductItemForm.builder()
        .productId(productId)
        .name(name)
        .price(10000)
        .count(10)
        .build();
  }
}