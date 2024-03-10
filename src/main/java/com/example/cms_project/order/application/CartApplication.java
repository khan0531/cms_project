package com.example.cms_project.order.application;

import static com.example.cms_project.common.exception.ErrorCode.ITEM_COUNT_NOT_ENOUGH;
import static com.example.cms_project.common.exception.ErrorCode.NOT_FOUND_PRODUCT;

import com.example.cms_project.common.exception.CustomException;
import com.example.cms_project.order.application.service.CartService;
import com.example.cms_project.order.application.service.ProductSearchService;
import com.example.cms_project.order.domain.product.AddProductCartForm;
import com.example.cms_project.order.domain.redis.Cart;
import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.entity.ProductItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartApplication {
  private final ProductSearchService productSearchService;
  private final CartService cartService;


  public Cart addCart(Long customerId, AddProductCartForm form) {
    Product product = productSearchService.getProductId(form.getId());
    if (product == null) {
      throw new CustomException(NOT_FOUND_PRODUCT);
    }

    Cart cart = cartService.getCart(customerId);

    if (cart != null && !addAble(cart, product, form)) {
      throw new CustomException(ITEM_COUNT_NOT_ENOUGH);
    }

    return cartService.addCart(customerId, form);
  }

  // 1. 잘바구니에 상품을 추가
  // 2. 상품의 가격, 수량이 변동 될 수 있음
  public Cart getCart(Long customerId) {
    Cart cart = refreeshCart(cartService.getCart(customerId));
    Cart returnCart = new Cart();
    returnCart.setCustomerId(customerId);
    returnCart.setProducts(cart.getProducts());
    returnCart.setMessages(cart.getMessages());
    cart.setMessages(new ArrayList<>());
    // 메세지를 확인했으니 없는 카트 반환
    cartService.putCart(customerId, cart);
    return returnCart;
  }

  public void clearCart(Long customerId) {
    cartService.putCart(customerId, null);
  }

  private Cart refreeshCart(Cart cart) {
    // 1. 상품이나 상품의 아이템의 정보, 가격, 수량이 변경되는지 체크
    // 2. 변경되었다면 메시지를 추가
    // 3. 확인한 메세지는 삭제
    // 4. 상품이나 상품의 아이템의 정보, 가격, 수량 변경
   Map<Long, Product> productMap = productSearchService.getListByProductIds(cart.getProducts().stream()
        .map(Cart.Product::getId)
        .collect(Collectors.toList()))
        .stream()
        .collect(Collectors.toMap(Product::getId, product -> product));

    for (int i = 0; i < cart.getProducts().size(); i++) {

      Cart.Product cartProduct = cart.getProducts().get(i);

      Product p = productMap.get(cartProduct.getId());
      if (p == null) {
        cart.getProducts().remove(cartProduct);
        i--;
        cart.addMessage(cartProduct.getName() + " 상품이 삭제되었습니다.");
        continue;
      }

      Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
          .collect(Collectors.toMap(ProductItem::getId, item -> item));

      List<String> tmpMessages = new ArrayList<>();
      for (int j = 0; j < cartProduct.getItems().size(); j++) {
        Cart.ProductItem cartProductItem = cartProduct.getItems().get(j);

        ProductItem pi = productItemMap.get(cartProductItem.getId());

        if (pi == null) {
          cartProduct.getItems().remove(cartProductItem);
          j--;
          tmpMessages.add(cartProductItem.getName() + " 옵션이 삭제되었습니다.");
          continue;
        }

        boolean isPriceChanged =false, isCountNotEnough = false;
        if (!cartProductItem.getPrice().equals(pi.getPrice())){
          isPriceChanged = true;
          cartProductItem.setPrice(pi.getPrice());
        }
        if(cartProductItem.getCount() > pi.getCount()) {
          isCountNotEnough = true;
          cartProductItem.setCount(pi.getCount());
        }
        if (isPriceChanged && isCountNotEnough) {
          tmpMessages.add(cartProductItem.getName() + " 가격 변동, 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
        } else if (isPriceChanged) {
          tmpMessages.add(cartProductItem.getName() + " 가격이 변동되었습니다.");
        } else if (isCountNotEnough) {
          tmpMessages.add(cartProductItem.getName() + " 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
        }
      }
      if (cartProduct.getItems().size() == 0) {
        cart.getProducts().remove(cartProduct);
        i--;
        cart.addMessage(cartProduct.getName() + " 상품의 옵션이 모두 없어져 구매가 불가능 합니다.");
        continue;
      }else if (tmpMessages.size() > 0) {
        StringBuilder builder = new StringBuilder();
        builder.append(cartProduct.getName() + " 상품의 변동 사항 : ");
        for (String message : tmpMessages) {
          builder.append(message).append(", ");
        }
        cart.addMessage(builder.toString());
      }
    }
    cartService.putCart(cart.getCustomerId(), cart);
    return cart;
  }

  private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
    Cart.Product cartProduct = cart.getProducts().stream()
        .filter(p -> p.getId().equals(form.getId()))
        .findFirst()
        .orElse(Cart.Product.builder().id(product.getId()).items(Collections.emptyList()).build());

    Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
        .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

    Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
        .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

    return form.getItems().stream().noneMatch(
        formItem -> {
          Integer cartCount = cartItemCountMap.get(formItem.getId());
          if (cartCount == null) {
            cartCount=0;
          }
          Integer currentCount = currentItemCountMap.get(formItem.getId());
          return formItem.getCount() + cartCount > currentCount;
        });
  }
}
