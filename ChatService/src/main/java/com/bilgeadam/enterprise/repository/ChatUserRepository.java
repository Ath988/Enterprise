package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {
	List<ChatUser> findByChatId(String chatId);
	List<ChatUser> findByUserId(Long userId);
	
	@Query("""
    SELECT COUNT(cu) > 0
    FROM ChatUser cu
    WHERE cu.chatId = :chatId AND cu.userId = :userId
""")
	boolean existsByChatIdAndUserId(@Param("chatId") String chatId, @Param("userId") Long userId);
	
	@Query("SELECT cu.userId FROM ChatUser cu WHERE cu.chatId = :chatId")
	List<Long> findUserIdsByChatId(@Param("chatId") String chatId);
	
}