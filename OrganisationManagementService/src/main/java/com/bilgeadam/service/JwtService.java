package com.bilgeadam.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class JwtService {
    //Token işlemlerini yürütebilmek için geçici olarak açıldı bu sınıf.
    private final String secretKey = "secretKey";
    private final String issuer = "EnterpriseApp";
    private final Long expiresAt = 1000L * 60 * 60; //60 dakika token süresi

    private final String USER_KEY = "USER_ENTERPRISE";

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    Date createdDate = new Date(System.currentTimeMillis());
    Date expirationDate = new Date(System.currentTimeMillis() + expiresAt);

    public String createUserToken(Long authId) {
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(createdDate)
                .withExpiresAt(expirationDate)
                .withClaim(USER_KEY, authId)
                .sign(algorithm);
    }

    public Optional<Long> validateToken(String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            if (Objects.isNull(jwt)) {
                return Optional.empty();
            }
            return Optional.of(jwt.getClaim(USER_KEY).asLong());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
