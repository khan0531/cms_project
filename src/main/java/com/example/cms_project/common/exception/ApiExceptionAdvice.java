package com.example.cms_project.common.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

  @ExceptionHandler({CustomException.class})
  public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(HttpServletRequest request, final CustomException c) {
    return ResponseEntity.status(c.getStatus())
        .body(CustomException.CustomExceptionResponse.builder()
            .status(c.getStatus())
            .code(c.getErrorCode().name())
            .message(c.getMessage())
            .build());
  }

}
