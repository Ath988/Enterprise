package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message,String> {

}