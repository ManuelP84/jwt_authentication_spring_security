package com.springsecurity.jwtAuthentication.security;

import com.springsecurity.jwtAuthentication.entity.user.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final long EXPIRE_DURATION = 24*60*60*1000; // Equivalent to 24 hrs
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getId() + "," + user.getEmail())
                .claim("roles", user.getRoles().toString())
                .setIssuer("Mpineda")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            LOGGER.error("JWT expired!", e);
        } catch ( IllegalArgumentException e) {
            LOGGER.error("Token is null or empty!", e);
        } catch (MalformedJwtException e){
            LOGGER.error("JWT is invalid", e);
        } catch (UnsupportedJwtException e){
            LOGGER.error("JWT is not supported");
        } catch (SignatureException e){
            LOGGER.error("Signature validation failed", e);
        }
        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }
    private Claims parseClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
