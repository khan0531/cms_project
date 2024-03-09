package com.example.cms_project.user.client.security;

import com.example.cms_project.user.client.domain.UserVo;
import com.example.cms_project.user.client.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Value;

public class JwtAuthenticationProvider {

  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Value("${jwt.expiration}")
  private long JWT_EXPIRATION;

  private String createToken(String userPk, Long id, UserType userType) {
    Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk)).setId(id.toString());
    claims.put("role", userType);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + JWT_EXPIRATION))
        .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
        .compact();
  }

  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public UserVo getUserVo(String token) {
    Claims c = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    return new UserVo(Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(c.getId()))), Aes256Util.decrypt(c.getSubject()));
  }

}
