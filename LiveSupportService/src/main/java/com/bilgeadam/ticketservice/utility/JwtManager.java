package com.bilgeadam.ticketservice.utility;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.ticketservice.exception.EnterpriseException;
import com.bilgeadam.ticketservice.exception.ErrorType;
import com.bilgeadam.ticketservice.utility.enums.ERole;


import java.util.Date;
import java.util.Optional;

public class JwtManager {
    private final String SECRETKEY ="secretkey";
    private final String ISSUER ="EnterpriseApp";
    private final Long EXDATE = 1000L * 60 * 60 ; // 5 minutes

    public Optional<String> createToken (Long authId, ERole role){
        String token;
        try{
            token = com.auth0.jwt.JWT.create().withAudience()
                    .withClaim("id", authId)
                    .withClaim("role", role.toString())
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXDATE))
                    .sign(Algorithm.HMAC512(SECRETKEY));
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<Long> validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT == null)
                return Optional.empty();
            Long authId = decodedJWT.getClaim("id").asLong();
            return Optional.of(authId);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<Long> getIdFromToken(String token){
        try {
            Algorithm algorithm=Algorithm.HMAC512(SECRETKEY);
            JWTVerifier verifier=com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT= verifier.verify(token);

            if (decodedJWT==null){
                throw new EnterpriseException(ErrorType.INVALID_TOKEN);
            }

            Long id=decodedJWT.getClaim("id").asLong();
            return Optional.of(id);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
    }
    public ERole getRoleFromToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) {
                System.out.println("Token Null ...");
                throw new EnterpriseException(ErrorType.INVALID_TOKEN);
            }

            String role = decodedJWT.getClaim("ROLE").asArray(String.class)[0];
            return ERole.valueOf(role.toUpperCase());
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("yoksa bura mıı????");
            throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        }
    }

    public boolean validateSystemAdmin(String token){
        return getRoleFromToken(token) == ERole.SYSTEM_ADMIN;
    }

    public Long getIdFromTokenIfSystemAdmin(String token){
        Optional<Long> optSystemAdminId = getIdFromToken(token);
        if (optSystemAdminId.isEmpty()) throw new EnterpriseException(ErrorType.INVALID_TOKEN);
        else if (!validateSystemAdmin(token)) throw new EnterpriseException(ErrorType.UNAUTHORIZED);
        else return optSystemAdminId.get();
    }
}
