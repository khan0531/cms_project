package com.example.cms_project.user.client.repository;

import com.example.cms_project.user.client.model.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

  Optional<Seller> findByIdAndEmail(Long id, String email);

  Optional<Seller> findByEmailAndPasswordAndVerifyIsTrue(String email, String password);

  Optional<Seller> findByEmail(String email);
}
