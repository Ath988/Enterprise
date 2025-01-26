package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,String> {
	
	
	@Query("SELECT m FROM Message m " +
			"WHERE m.chat.id = :chatId " +
			"ORDER BY m.timeStamp DESC")
	Page<Message> findLastMessagesByChatId(@Param("chatId") String chatId, Pageable pageable);

	
}