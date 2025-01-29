package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.dto.response.ChatListViewDto;
import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.EChatType;
import com.bilgeadam.enterprise.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRepository extends JpaRepository<Chat,String> {
	
	@Query("""
    SELECT c FROM Chat c
    JOIN FETCH c.users u
    WHERE c.isDeleted = false
    AND c.id = :chatId
    """)
	Optional<Chat> findChatWithUsersById(@Param("chatId") String chatId);

	
	
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
                WHERE u.id IN (SELECT cu.id FROM c.users cu WHERE cu.id <> :userId)
                ORDER BY u.id ASC LIMIT 1
            )
        END,
        c.createDate,
        (SELECT m.content FROM Message m WHERE m.chat.id = c.id ORDER BY m.timeStamp DESC LIMIT 1)
    )
    FROM Chat c
    JOIN c.users u
    WHERE c.isDeleted = false AND u.id = :userId
    ORDER BY c.createDate DESC
    LIMIT :limit
""")
	List<ChatListViewDto> findTopChatsByUser(@Param("userId") String userId, @Param("limit") int limit);
	
	
	
	
	
	
	
	
	
	
	Boolean existsChatById(String id);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
			"FROM Chat c " +
			"WHERE c.isDeleted = false AND SIZE(c.users) = :size " +
			"AND :users MEMBER OF c.users")
	Boolean existsPrivateChatByUsers(@Param("users") Set<User> users, @Param("size") long size);
	
	@Query("SELECT c FROM Chat c " +
			"JOIN c.users u " +
			"WHERE c.isDeleted = false " +
			"AND c.eChatType = :chatType " +
			"AND SIZE(c.users) = :size " +
			"AND u IN :users " +
			"GROUP BY c " +
			"HAVING COUNT(u) = :size")
	Optional<Chat> findPrivateChatByUsers(@Param("users") Set<User> users,
	                                      @Param("size") long size,
	                                      @Param("chatType") EChatType chatType);

	
	
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Chat c JOIN c.users u WHERE c.isDeleted = false AND c.id = :chatId AND u.id = :userId")
	boolean isUserInChat(@Param("chatId") String chatId, @Param("userId") String userId);
	
	
	
}