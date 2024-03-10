package com.example.cms_project.order.entity;

import com.example.cms_project.common.entity.BaseEntity;
import com.example.cms_project.order.domain.product.AddProductItemForm;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long sellerId;

  private String name;

  @Audited // 변경이력을 남기기 위해 추가
  private Integer price;

  @Audited
  private Integer count; // 재고

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public static ProductItem of(Long sellerId, AddProductItemForm form) {
    return ProductItem.builder()
        .sellerId(sellerId)
        .name(form.getName())
        .price(form.getPrice())
        .count(form.getCount())
        .build();
  }
}
