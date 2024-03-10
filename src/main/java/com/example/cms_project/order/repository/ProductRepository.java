package com.example.cms_project.order.repository;

import com.example.cms_project.order.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

  @EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<Product> findBySellerIdAndId(Long sellerId, Long id);

  @EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<Product> findWithProductItemsById(Long id);

  @EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
  List<Product> findAllByIdIn(List<Long> productIds);
}
