package com.example.cms_project.order.application.service;

import com.example.cms_project.common.config.RedisClient;
import com.example.cms_project.order.domain.product.AddProductCartForm;
import com.example.cms_project.order.domain.redis.Cart;
import com.example.cms_project.order.domain.redis.Cart.Product;
import com.example.cms_project.order.domain.redis.Cart.ProductItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

  private final RedisClient redisClient;

  public Cart getCart(Long customerId) {
    return redisClient.get(customerId, Cart.class);
  }

  public Cart addCart(Long customerId, AddProductCartForm form) {
    Cart cart = redisClient.get(customerId, Cart.class);
    if (cart == null) {
      cart = Cart.builder().customerId(customerId).build();
    }
    // 이전에 같은 상품이 있는지 확인
    Optional<Product> productOptional = cart.getProducts().stream()
        .filter(product -> product.getId().equals(form.getId()))
        .findFirst();

    if (productOptional.isPresent()) {
      Cart.Product redisProduct = productOptional.get();
      // requested product
      List<ProductItem> items = form.getItems().stream().map(Cart.ProductItem::from).collect(
          Collectors.toList());
      // 검색 속도 빠르게 하려고 map 사용
      Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
          .collect(Collectors.toMap(it->it.getId(), it->it));

      if (redisProduct.getItems().equals(form.getItems())) {
        cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
      }
      for (Cart.ProductItem item : items) {
        Cart.ProductItem redisItem = redisItemMap.get(item.getId());

        if (redisItem == null) {
          // happy case
          redisProduct.getItems().add(item);
        }else  {
          // sad case
          if (!redisItem.getPrice().equals(item.getPrice())) {
            cart.addMessage(redisProduct.getName() + "의 가격이 변경되었습니다. 확인 부탁드립니다.");
          }
          redisItem.setCount(redisItem.getCount() + item.getCount());
        }
      }
    } else {
      Cart.Product product = Product.from(form);
      cart.getProducts().add(product);
    }

    redisClient.put(customerId, cart);
    return cart;

  }




}
