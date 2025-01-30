package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.dto.response.ChatListViewDto;
import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.EChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRepository extends JpaRepository<Chat,String> {
	
	@Query("""
    SELECT c FROM Chat c
    WHERE c.isDeleted = false
    AND c.id = :chatId
    AND EXISTS (SELECT 1 FROM ChatUser cu WHERE cu.chatId = c.id)
""")
	Optional<Chat> findChatByIdWithAtLeastOneUser(@Param("chatId") String chatId);


	
	
	@Query("SELECT c FROM Chat AS c WHERE c.id = :chatId AND c.isDeleted = false")
	Optional<Chat> findChatById(@Param("chatId") String chatId);
	
	
	@Query("""
    SELECT new com.bilgeadam.enterprise.dto.response.ChatListViewDto(
        c.id,
        CASE
            WHEN c.eChatType = 'GROUP' THEN c.name
            ELSE (
                SELECT CONCAT(u.name, ' ', u.surname)
                FROM User u
                WHERE u.id = (
                    SELECT cu.userId
                    FROM ChatUser cu
                    WHERE cu.chatId = c.id AND cu.userId <> :userId
                    ORDER BY cu.userId ASC
                    LIMIT 1
                )
            )
        END,
        c.createDate,
        (SELECT m.content FROM Message m WHERE m.chatId = c.id ORDER BY m.timeStamp DESC LIMIT 1)
    )
    FROM Chat c
    JOIN ChatUser cu ON c.id = cu.chatId
    WHERE c.isDeleted = false AND cu.userId = :userId
    ORDER BY c.createDate DESC
    LIMIT :limit
""")
	List<ChatListViewDto> findTopChatsByUser(@Param("userId") String userId, @Param("limit") int limit);
	
	
	
	Boolean existsChatById(String id);
	
	
	@Query("""
    SELECT c FROM Chat c
    JOIN ChatUser cu ON cu.chatId = c.id
    WHERE c.isDeleted = false
    AND c.eChatType = :chatType
    AND (SELECT COUNT(DISTINCT cu2.userId) FROM ChatUser cu2 WHERE cu2.chatId = c.id) = :size
    GROUP BY c.id
    HAVING COUNT(DISTINCT cu.userId) = :size
""")
	Optional<Chat> findPrivateChatByUsers(
			@Param("userIds") Set<String> userIds,
			@Param("size") long size,
			@Param("chatType") EChatType chatType
	);

	
	
	
	
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Chat c JOIN c.users u WHERE c.isDeleted = false AND c.id = :chatId AND u.id = :userId")
	boolean isUserInChat(@Param("chatId") String chatId, @Param("userId") String userId);
	
	
	
}