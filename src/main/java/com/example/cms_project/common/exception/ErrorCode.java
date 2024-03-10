package com.example.cms_project.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
  ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증된 회원입니다."),
  WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),
  EXPIRED_CODE(HttpStatus.BAD_REQUEST, "만료된 인증 코드입니다."),

  //login
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "로그인 정보를 확인해주세요."),

  //balance
  NOT_ENOUGH_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),

  //order
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
