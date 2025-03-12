package com.bilgeadam.util;


import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.bilgeadam.dto.response.UserPermissionResponse;
import com.bilgeadam.exception.EnterpriseException;
import com.bilgeadam.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class JwtManager {
    private final String SECRETKEY ="secretkey";
    private final String ISSUER ="EnterpriseApp";

    Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);


    public Optional<String> createToken (Long authId, Set<String> roles,Set<String> permissions,String subscriptionType){
        Date createdDate = new Date(System.currentTimeMillis());
        long EXDATE = 1000L * 60 * 60;
        Date expirationDate = new Date(System.currentTimeMillis() + EXDATE);
        String token;
        try{
            token = com.auth0.jwt.JWT.create()
                    .withClaim("id", authId)
                    .withArrayClaim("ROLE", roles.toArray(new String[0]))
                    .withArrayClaim("PERMISSION", permissions.toArray(new String[0]))
                    .withClaim("SUBSCRIPTION_TYPE", subscriptionType)
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


    public UserPermissionResponse getRolesAndPermissionsFromToken(String token){

        try{
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            List<String> roles = Optional.ofNullable(decodedJWT.getClaim("ROLE").asList(String.class)).orElse(new ArrayList<>());
            List<String> permissions = Optional.ofNullable(decodedJWT.getClaim("PERMISSION").asList(String.class)).orElse(new ArrayList<>());
            String subscriptonPlan = Optional.ofNullable(decodedJWT.getClaim("SUBSCRIPTION_TYPE").asString()).orElse("");
            return new UserPermissionResponse(new HashSet<>(roles), new HashSet<>(permissions),subscriptonPlan);

        }catch (Exception e){
            log.error("getRolesAndPermissionsFromToken Hata!!: {}", e.getMessage());
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
    }

}