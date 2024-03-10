package com.example.cms_project.user.client.application.service.customer;

import com.example.cms_project.user.client.entity.Customer;
import com.example.cms_project.user.client.repository.CustomerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;

  public Optional<Customer> findByIdAndEmail(Long id, String email) {
    return customerRepository.findById(id).stream()
        .filter(customer -> customer.getEmail().equals(email))
        .findFirst();
  }

  public Optional<Customer> findValidCustomer(String email, String password) {
    return customerRepository.findByEmail(email).stream()
        .filter(customer -> customer.getPassword().equals(password)&&customer.isVerify())
        .findFirst();

  }
}
