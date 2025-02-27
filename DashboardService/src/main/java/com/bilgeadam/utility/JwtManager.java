package com.bilgeadam.utility;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.dto.response.otherServices.UserPermissionResponse;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.DashboardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class JwtManager {
    private final String SECRETKEY ="secretkey";
    private final String ISSUER ="EnterpriseApp";

    Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);

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
            throw new DashboardException(ErrorType.INVALID_TOKEN);
        }
    }

}
