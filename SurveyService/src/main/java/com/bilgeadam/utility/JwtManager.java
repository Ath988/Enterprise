package com.bilgeadam.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.SurveyServiceException;
import com.bilgeadam.utility.enums.ERole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtManager {
    private final String SECRETKEY = "secretkey";
    private final String ISSUER = "EnterpriseApp";
    private final Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);
    
    public Map<String, Object> validateTokenAndGetClaims(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer(ISSUER)
                                      .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            
            // Rol ve yetkileri al
            List<String> roles = decodedJWT.getClaim("ROLE").asList(String.class);
            List<String> permissions = decodedJWT.getClaim("PERMISSION").asList(String.class);
            
            if (roles == null || roles.isEmpty()) {
                throw new SurveyServiceException(ErrorType.UNAUTHORIZED);
            }
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", decodedJWT.getClaim("id").asLong());
            claims.put("roles", roles);
            claims.put("permissions", permissions);
            claims.put("subscriptionType", decodedJWT.getClaim("SUBSCRIPTION_TYPE").asString());
            
            return claims;
        } catch (Exception e) {
            log.error("Token doğrulama hatası: ", e);
            throw new SurveyServiceException(ErrorType.INVALID_TOKEN);
        }
    }
    
    public Optional<Long> validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer(ISSUER)
                                      .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null) {
                return Optional.empty();
            }
            Long authId = decodedJWT.getClaim("id").asLong();
            return Optional.of(authId);
        } catch (Exception e) {
            log.error("validateToken Hatası: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    public boolean hasRole(String token, String... expectedRoles) {
        try {
            Map<String, Object> claims = validateTokenAndGetClaims(token);
            @SuppressWarnings("unchecked")
            List<String> userRoles = (List<String>) claims.get("roles");
            return Arrays.stream(expectedRoles)
                         .anyMatch(userRoles::contains);
        } catch (Exception e) {
            log.error("Rol kontrolü hatası: {}", e.getMessage());
            return false;
        }
    }
    
    public List<String> getRoles(String token) {
        try {
            Map<String, Object> claims = validateTokenAndGetClaims(token);
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");
            return roles != null ? roles : new ArrayList<>();
        } catch (Exception e) {
            log.error("Rolleri alma hatası: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<String> getPermissions(String token) {
        try {
            Map<String, Object> claims = validateTokenAndGetClaims(token);
            @SuppressWarnings("unchecked")
            List<String> permissions = (List<String>) claims.get("permissions");
            return permissions != null ? permissions : new ArrayList<>();
        } catch (Exception e) {
            log.error("Yetkileri alma hatası: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<String> getRolesFromToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer(ISSUER)
                                      .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            
            return decodedJWT.getClaim("ROLE").asList(String.class);
        } catch (Exception e) {
            log.error("Token'dan rol alma hatası: ", e);
            throw new SurveyServiceException(ErrorType.INVALID_TOKEN);
        }
    }
}