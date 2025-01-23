package com.bilgeadam.repository;

import com.bilgeadam.entity.UserAuthVerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserAuthVerifyCodeRepository extends JpaRepository<UserAuthVerifyCode, Integer> {
    @Query(value = "SELECT U.userId FROM UserAuthVerifyCode U WHERE  U.code  = :authCode  order by U.create_at DESC LIMIT 1")
    Optional<Long> findLastUserIdByAuthCode(String authCode);
}
