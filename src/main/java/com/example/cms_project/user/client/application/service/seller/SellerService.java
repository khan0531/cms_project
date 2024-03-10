package com.example.cms_project.user.client.application.service.seller;

import static com.example.cms_project.user.client.exception.ErrorCode.ALREADY_VERIFY;
import static com.example.cms_project.user.client.exception.ErrorCode.EXPIRED_CODE;
import static com.example.cms_project.user.client.exception.ErrorCode.NOT_FOUND_USER;
import static com.example.cms_project.user.client.exception.ErrorCode.WRONG_VERIFICATION;

import com.example.cms_project.user.client.domain.SignUpForm;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.entity.Seller;
import com.example.cms_project.user.client.repository.SellerRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

  private final SellerRepository sellerRepository;

  public Optional<Seller> findByIdAndEmail(Long id, String email) {
      return sellerRepository.findByIdAndEmail(id, email);
  }

  public Optional<Seller> findValidSeller(String email, String password) {
      return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password);
  }

  public Seller signUp(SignUpForm form) {
      return sellerRepository.save(Seller.from(form));
  }

  public boolean isEmailExist(String email) {
      return sellerRepository.findByEmail(email).isPresent();
  }

  @Transactional
  public void verifyEmail(String email, String code) {
    Seller seller = sellerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    if (seller.isVerify()) {
        throw new CustomException(ALREADY_VERIFY);
    }
    if (!seller.getVerificationCode().equals(code)) {
        throw new CustomException(WRONG_VERIFICATION);
    }
    if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
        throw new CustomException(EXPIRED_CODE);
    }
    seller.setVerify(true);
  }

  @Transactional
  public LocalDateTime changeSellerValidateEmail(Long customerId, String verificationCode) {
    Optional<Seller> sellerOptional = sellerRepository.findById(customerId);

    if(sellerOptional.isPresent()) {
      Seller seller = sellerOptional.get();
      seller.setVerificationCode(verificationCode);
      seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
      return seller.getVerifyExpiredAt();
    }
    throw new CustomException(NOT_FOUND_USER);
  }
}
