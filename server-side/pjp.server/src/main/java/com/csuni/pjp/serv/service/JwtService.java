package com.csuni.pjp.serv.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;


@Service

public class JwtService {
    @Value("${jwt.secret}")
    private  String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;
    @Value("${jwt.gracePeriod}")
    private long jwtGracePeriodMs;


    @Setter
    private Clock clock = Clock.systemDefaultZone();

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {

        Date now = Date.from(clock.instant());
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String expectedUsername) {
        String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
         .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public Date getExpirationDateAsDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenWithinGracePeriod(String token) {
        try {
            Date expDate = getExpirationDateAsDate(token);
            long now = System.currentTimeMillis();
            return now <= expDate.getTime() + jwtGracePeriodMs;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            long expTime = e.getClaims().getExpiration().getTime();
            long now = System.currentTimeMillis();
            return now <= expTime + jwtGracePeriodMs;
        } catch (Exception e) {

            return false;
        }
    }



}