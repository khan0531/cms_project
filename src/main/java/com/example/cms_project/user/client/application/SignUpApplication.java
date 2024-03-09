package com.example.cms_project.user.client.application;

import com.example.cms_project.user.client.domain.SendMailForm;
import com.example.cms_project.user.client.domain.SignUpForm;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.exception.ErrorCode;
import com.example.cms_project.user.client.mailgun.MailgunClient;
import com.example.cms_project.user.client.model.Customer;
import com.example.cms_project.user.client.service.SignUpCustomerService;
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

  @Value("${mailgun.email}")
  private String MAILGUN_EMAIL;

  public void customerVerify(String email, String code) {
    signUpCustomerService.verifyEmail(email, code);
  }

  public String customerSignUp(SignUpForm form) {
    if(signUpCustomerService.isEmailExists(form.getEmail())) {
      //exception
      throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
    } else {
      Customer c = signUpCustomerService.signUp(form);
      LocalDateTime now = LocalDateTime.now();

      String code = getRandomCode();
      SendMailForm sendMailForm = SendMailForm.builder()
          .from(MAILGUN_EMAIL)
          .to(form.getEmail())
          .subject("Verification Email")
          .text(getVerificationEmailBody(c.getEmail(), c.getName(), code))
          .build();
      log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm));
      signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
      return "success";
    }

  }

  private String getRandomCode() {
    return RandomStringUtils.random(10, true, true);
  }

  private String getVerificationEmailBody(String email, String name, String code) {
    StringBuilder builder = new StringBuilder();
    return builder.append("Hello")
        .append(name).append("! Please Click the link below to verify your email address.\n\n")
        .append("http://localhost:8081/signup/verify/customer?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }
}
