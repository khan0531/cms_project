package com.example.cms_project.order.domain.product;

import com.example.cms_project.order.entity.Product;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private List<ProductItemDto> items;


    public static ProductDto from(Product product) {
      return ProductDto.builder()
          .id(product.getId())
          .name(product.getName())
          .description(product.getDescription())
          .items(product.getProductItems().stream()
              .map(ProductItemDto::from)
              .collect(Collectors.toList()))
          .build();
    }
}
