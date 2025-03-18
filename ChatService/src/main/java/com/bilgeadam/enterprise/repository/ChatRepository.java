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
        CASE
            WHEN c.eChatType = 'GROUP' THEN c.name
            ELSE null
        END,
        COALESCE(latestMessage.lastMessageDate, c.createDate),
        COALESCE(latestMessage.lastMessage, ''),
        c.chatImage,
        false,
        c.isSupportChat,
        cu2.userId
    )
    FROM Chat c
    JOIN ChatUser cu ON c.id = cu.chatId
    LEFT JOIN ChatUser cu2 ON cu2.chatId = c.id AND cu2.userId <> :userId
    LEFT JOIN (
        SELECT m2.chatId AS chatId,
               m2.content AS lastMessage,
               m2.timeStamp AS lastMessageDate,
               ROW_NUMBER() OVER (PARTITION BY m2.chatId ORDER BY m2.timeStamp DESC) AS row_num
        FROM Message m2
    ) latestMessage ON latestMessage.chatId = c.id AND latestMessage.row_num = 1
    WHERE c.isDeleted = false
    AND (
        cu.userId = :userId OR c.eChatType = 'PRIVATE'
    )
    ORDER BY COALESCE(latestMessage.lastMessageDate, c.createDate) DESC
    LIMIT :limit
""")
	List<ChatListViewDto> findTopChatsByUser(@Param("userId") Long userId, @Param("limit") int limit);

	
	@Query("""
    SELECT new com.bilgeadam.enterprise.view.ChatUserInfo(
        c.id, c.createDate, c.description, c.name, cu.userId
    )
    FROM Chat c
    JOIN ChatUser cu ON cu.chatId = c.id
    WHERE c.id = :chatId
""")
	List<ChatUserInfo> findChatUserInfoByChatId(@Param("chatId") String chatId);
	
	
	
	
	
	
	
	
	
	
	
	
	Boolean existsChatById(String id);
	
	
	@Query("""
    SELECT c FROM Chat c
    JOIN ChatUser cu ON cu.chatId = c.id
    WHERE c.isDeleted = false
    AND c.eChatType = :chatType
    AND cu.userId IN (:userIds)
    GROUP BY c.id
    HAVING COUNT(DISTINCT cu.userId) = :size
""")
	Optional<Chat> findPrivateChatByUsers(
			@Param("userIds") List<Long> userIds,
			@Param("size") long size,
			@Param("chatType") EChatType chatType
	);
	
	
	
}