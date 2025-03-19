package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.Message;
import com.bilgeadam.enterprise.view.MessageView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,String> {
	
	
	@Query("""
    SELECT m FROM Message m
    WHERE m.chatId = :chatId
    AND m.isDeleted = false
    ORDER BY m.timeStamp DESC
    LIMIT :size
""")
	List<Message> findLastMessagesByChatId(@Param("chatId") String chatId, @Param("size") int size);


	
	@Query("SELECT m FROM Message AS m WHERE m.id = :messageId AND m.isDeleted = false")
	Optional<Message> findMessageById(@Param("messageId") String messageId);
	
	@Query("SELECT m FROM Message m " +
			"WHERE m.chatId = :chatId AND m.isDeleted=false AND m.timeStamp < :lastTimestamp ORDER BY m.timeStamp DESC")
	List<Message> findMessagesByChatIdBefore(@Param("chatId") String chatId, Pageable pageable, @Param("lastTimestamp") LocalDateTime lastTimestamp);
	
	
	
}