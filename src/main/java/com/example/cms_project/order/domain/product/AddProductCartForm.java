package com.example.cms_project.order.domain.product;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductCartForm {
  private Long id;
  private Long sellerId;
  private String name;
  private String description;
  private List<ProductItem> items = new ArrayList<>();

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class ProductItem {
    private Long id;
    private String name;
    private Integer count;
    private Integer price;
  }

}
