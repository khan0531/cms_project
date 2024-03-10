package com.example.cms_project.user.client.application;

import static com.example.cms_project.user.client.exception.ErrorCode.ALREADY_REGISTER_USER;

import com.example.cms_project.user.client.domain.SendMailForm;
import com.example.cms_project.user.client.domain.SignUpForm;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.mailgun.MailgunClient;
import com.example.cms_project.user.client.entity.Customer;
import com.example.cms_project.user.client.entity.Seller;
import com.example.cms_project.user.client.application.service.customer.SignUpCustomerService;
import com.example.cms_project.user.client.application.service.seller.SellerService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {
  private final MailgunClient mailgunClient;
  private final SignUpCustomerService signUpCustomerService;
  private final SellerService sellerService;

  @Value("${mailgun.email}")
  private String MAILGUN_EMAIL;

  public void customerVerify(String email, String code) {
    signUpCustomerService.verifyEmail(email, code);
  }

  public String customerSignUp(SignUpForm form) {
    if(signUpCustomerService.isEmailExists(form.getEmail())) {
      //exception
      throw new CustomException(ALREADY_REGISTER_USER);
    } else {
      Customer c = signUpCustomerService.signUp(form);
      LocalDateTime now = LocalDateTime.now();

      String code = getRandomCode();
      SendMailForm sendMailForm = SendMailForm.builder()
          .from(MAILGUN_EMAIL)
          .to(form.getEmail())
          .subject("Verification Email")
          .text(getVerificationEmailBody(c.getEmail(), c.getName(), "customer" ,code))
          .build();
      log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm));
      signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
      return "success";
    }
  }

  public void sellerVerify(String email, String code) {
    sellerService.verifyEmail(email, code);
  }

  public String sellerSignUp(SignUpForm form) {
    if(sellerService.isEmailExist(form.getEmail())) {
      //exception
      throw new CustomException(ALREADY_REGISTER_USER);
    } else {
      Seller s = sellerService.signUp(form);
      LocalDateTime now = LocalDateTime.now();

      String code = getRandomCode();
      SendMailForm sendMailForm = SendMailForm.builder()
          .from(MAILGUN_EMAIL)
          .to(form.getEmail())
          .subject("Verification Email")
          .text(getVerificationEmailBody(s.getEmail(), s.getName(), "seller" ,code))
          .build();
      log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm));
      sellerService.changeSellerValidateEmail(s.getId(), code);
      return "success";
    }
  }

  private String getRandomCode() {
    return RandomStringUtils.random(10, true, true);
  }

  private String getVerificationEmailBody(String email, String name, String type , String code) {
    StringBuilder builder = new StringBuilder();
    return builder.append("Hello")
        .append(name).append("! Please Click the link below to verify your email address.\n\n")
        .append("http://localhost:8080/signup/"+type+"/verify/?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }
}
