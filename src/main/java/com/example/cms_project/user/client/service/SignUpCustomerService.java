package com.example.cms_project.user.client.service;

import com.example.cms_project.user.client.domain.SignUpForm;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.exception.ErrorCode;
import com.example.cms_project.user.client.model.Customer;
import com.example.cms_project.user.client.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

  private final CustomerRepository customerRepository;

  public Customer signUp(SignUpForm form) {
    return customerRepository.save(Customer.from(form));
  }

  public boolean isEmailExists(String email) {
    return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
  }

  @Transactional
  public void verifyEmail(String email, String code) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if(customer.isVerify()) {
      throw new CustomException(ErrorCode.ALREADY_VERIFY);
    }
    if(!customer.getVerificationCode().equals(code)) {
      throw new CustomException(ErrorCode.WRONG_VERIFICATION);
    }
    if(customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.EXPIRED_CODE);
    }
    customer.setVerify(true);
  }

  @Transactional
  public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
    Optional<Customer> customerOptional = customerRepository.findById(customerId);

    if(customerOptional.isPresent()) {
      Customer customer = customerOptional.get();
      customer.setVerificationCode(verificationCode);
      customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
      return customer.getVerifyExpiredAt();
    }
    throw new CustomException(ErrorCode.NOT_FOUND_USER);
  }
}
