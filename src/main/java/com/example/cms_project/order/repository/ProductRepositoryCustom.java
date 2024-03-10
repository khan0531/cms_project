package com.example.cms_project.order.repository;

import com.example.cms_project.order.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
  List<Product> searchByName(String name);
}
