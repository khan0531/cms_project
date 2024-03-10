package com.example.cms_project.order.repository;

import com.example.cms_project.order.entity.Product;
import com.example.cms_project.order.entity.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Product> searchByName(String name) {
    String search = "%" + name + "%";
    QProduct product = QProduct.product;
    return queryFactory.selectFrom(product)
        .where(product.name.like(search))
        .fetch();
  }
}
