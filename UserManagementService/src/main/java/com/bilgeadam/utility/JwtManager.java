package com.bilgeadam.utility;


import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Service
public class JwtManager {
    private final String SECRETKEY ="secretkey";
    private final String ISSUER ="EnterpriseApp";

    Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);

    //subscription servise tokenli istek atabilmek için yaratıldı.
    public Optional<String> createTokenForSubscription (Long authId, Set<String> roles, Set<String> permissions){
        Date createdDate = new Date(System.currentTimeMillis());
        long EXDATE = 1000L * 60 * 60;
        Date expirationDate = new Date(System.currentTimeMillis() + EXDATE);
        String token;
        try{
            token = com.auth0.jwt.JWT.create()
                    .withClaim("id", authId)
                    .withArrayClaim("ROLE", roles.toArray(new String[0]))
                    .withArrayClaim("PERMISSION", permissions.toArray(new String[0]))
                    .withIssuer(ISSUER)
                    .withIssuedAt(createdDate)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty();
        }
    }



    public Optional<Long> validateToken(String token){
        try{
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT == null)
                return Optional.empty();
            Long authId = decodedJWT.getClaim("id").asLong();
            return Optional.of(authId);
        }catch (Exception e){
            log.error("validateToken Hata!!: {}", e.getMessage());
            return Optional.empty();
        }
    }



}