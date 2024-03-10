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
  ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다."),
  NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
  NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
  SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "같은 이름의 상품이 이미 존재합니다."),
  CART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "장바구니 변경에 실패했습니다."),
  ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),;

  private final HttpStatus httpStatus;
  private final String detail;
}
