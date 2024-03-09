package com.example.cms_project.user.client.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Aes256UtilTest {

  @Test
  void encrypt() {
    String encrypted = Aes256Util.encrypt("hello world");
    assertEquals("hello world", Aes256Util.decrypt(encrypted));
  }
}