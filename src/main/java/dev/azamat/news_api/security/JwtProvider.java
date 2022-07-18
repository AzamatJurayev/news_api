package dev.azamat.news_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final String code = "000topolmayo'l";
    private final Long time = 3600000l;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .signWith(SignatureAlgorithm.ES512,code).compact();

    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(code)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

}
