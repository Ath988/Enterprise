package com.bilgeadam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "secretKey";
    private static final String ISSUER = "EnterpriseApp";
    private static final String USER_ID_CLAIM = "USER_ENTERPRISE";
    private static final long EXPIRATION_TIME = 1000L * 60 * 60;

    private final String USER_KEY = "USER_ENTERPRISE";

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String createUserToken(Long userId) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim(USER_ID_CLAIM, userId)
                .sign(algorithm);
    }

    public Optional<Long> extractUserId(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);

            return Optional.ofNullable(jwt.getClaim(USER_ID_CLAIM).asLong());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        return extractUserId(token).isPresent();
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
