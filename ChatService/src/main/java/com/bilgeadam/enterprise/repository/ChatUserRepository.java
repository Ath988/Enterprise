package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {
	List<ChatUser> findByChatId(String chatId);
	List<ChatUser> findByUserId(String userId);
	
	@Query("""
    SELECT COUNT(cu) > 0
    FROM ChatUser cu
    WHERE cu.chatId = :chatId AND cu.userId = :userId
""")
	boolean existsByChatIdAndUserId(@Param("chatId") String chatId, @Param("userId") String userId);
	
	@Query("SELECT cu.userId FROM ChatUser cu WHERE cu.chatId = :chatId")
	List<String> findUserIdsByChatId(@Param("chatId") String chatId);
	
	Optional<ChatUser> findChatUserByChatIdAndUserId(String userId, String chatId);

}