package com.bilgeadam.enterprise.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class JwtManager {
	private final String SECRETKEY ="secretkey";
	private final String ISSUER ="EnterpriseApp";
	private final Long exDate=1000000L*60;
	
	public String createToken(String authId){
		Date createdDate=new Date(System.currentTimeMillis());
		Date expirationDate=new Date(System.currentTimeMillis()+exDate);
		Algorithm algorithm= Algorithm.HMAC512(SECRETKEY);
		String token = JWT.create()
		                  .withAudience()
		                  .withIssuer(ISSUER)
		                  .withIssuedAt(createdDate)
		                  .withExpiresAt(expirationDate)
		                  .withClaim("id", authId)
		                  .withClaim("key","JX_15_TJJJ")
		                  .sign(algorithm);
		return token;
	}
	
	public Optional<Long> validateToken(String token){
		try{
			Algorithm algorithm=Algorithm.HMAC512(SECRETKEY);
			JWTVerifier verifier=JWT.require(algorithm).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			if (Objects.isNull(decodedJWT))
				return Optional.empty();
			Long authId = decodedJWT.getClaim("id").asLong();
			return Optional.of(authId);
		}
		catch (Exception exception) {
			return Optional.empty();
		}
		
	}
}