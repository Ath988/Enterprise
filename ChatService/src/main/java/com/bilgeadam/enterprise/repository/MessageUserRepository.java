package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.MessageUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageUserRepository extends JpaRepository<MessageUser,String> {
}