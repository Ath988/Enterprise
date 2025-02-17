package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.dto.response.ChatListViewDto;
import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.EChatType;
import com.bilgeadam.enterprise.view.ChatUserInfo;
import com.bilgeadam.enterprise.view.ChatView;
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
    SELECT DISTINCT new com.bilgeadam.enterprise.dto.response.ChatListViewDto(
        c.id,
        c.eChatType,
        COALESCE(
            CASE
                WHEN c.eChatType = 'GROUP' THEN c.name
                ELSE CONCAT(u2.name, ' ', u2.surname)
            END, 'Unknown'
        ),
        COALESCE(latestMessage.lastMessageDate, c.createDate),
        COALESCE(latestMessage.lastMessage, ''),
        COALESCE(
            CASE
                WHEN c.eChatType = 'GROUP' THEN c.chatImage
                ELSE u2.profilePicture
            END, ''
        ),
        CASE
            WHEN c.eChatType = 'GROUP' THEN false
            ELSE COALESCE(u2.isOnline, false)
        END,
        CASE
        	WHEN c.eChatType = 'GROUP' THEN null
        	ELSE u2.id
        END
    )
    
    FROM Chat c
    JOIN ChatUser cu ON c.id = cu.chatId
    LEFT JOIN ChatUser cu2 ON cu2.chatId = c.id AND cu2.userId <> :userId
    LEFT JOIN User u2 ON u2.id = cu2.userId
    LEFT JOIN (
        SELECT m2.chatId AS chatId,
               m2.content AS lastMessage,
               m2.timeStamp AS lastMessageDate,
               ROW_NUMBER() OVER (PARTITION BY m2.chatId ORDER BY m2.timeStamp DESC) AS row_num
        FROM Message m2
    ) latestMessage ON latestMessage.chatId = c.id AND latestMessage.row_num = 1
    WHERE c.isDeleted = false AND cu.userId = :userId
    ORDER BY COALESCE(latestMessage.lastMessageDate, c.createDate) DESC
    LIMIT :limit
""")
	List<ChatListViewDto> findTopChatsByUser(@Param("userId") String userId, @Param("limit") int limit);
	
	@Query("SELECT new com.bilgeadam.enterprise.view.ChatUserInfo(" +
			"   c.id, c.createDate, c.description, c.name, " +
			"   u.id, u.name, u.surname, u.isOnline, u.profilePicture" +
			") " +
			"FROM Chat c, ChatUser cu, User u " +
			"WHERE c.id = :chatId " +
			"  AND cu.chatId = c.id " +
			"  AND u.id = cu.userId")
	List<ChatUserInfo> findChatUserInfoByChatId(@Param("chatId") String chatId);
	
	
	
	
	
	
	
	
	
	
	
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
	
	
	
	
}