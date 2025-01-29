package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,String> {
	
	
	@Query("""
    SELECT m FROM Message m
    WHERE m.chat.id = :chatId
    AND m.isDeleted = false
    ORDER BY m.timeStamp DESC
""")
	List<Message> findLastMessagesByChatId(@Param("chatId") String chatId);

	
	@Query("SELECT m FROM Message AS m WHERE m.id = :messageId AND m.isDeleted = false")
	Optional<Message> findMessageById(@Param("messageId") String messageId);

	
}