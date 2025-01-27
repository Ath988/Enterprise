package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.view.ChatListView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRepository extends JpaRepository<Chat,String> {
	
	@Query("SELECT c FROM Chat c JOIN FETCH c.users WHERE c.id = :chatId")
	Optional<Chat> findChatWithUsersById(@Param("chatId") String chatId);
	
	Optional<Chat> findChatById(String id);
	
	@Query("SELECT new com.bilgeadam.enterprise.view.ChatListView(" +
			"c.id, c.name, " +
			"(SELECT CONCAT(u.name, ' ', u.surname) FROM User u JOIN c.users cu WHERE u != ?1), " +
			"c.createDate, " +
			"(SELECT m.content FROM Message m WHERE m.chat.id = c.id ORDER BY m.timeStamp DESC LIMIT 1)" +
			") " +
			"FROM Chat c JOIN c.users u WHERE u = ?1 " +
			"ORDER BY c.createDate DESC")
	List<ChatListView> findChatListViewByUserOrderByCreateDateDesc(User user);
	
	Boolean existsChatById(String id);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
			"FROM Chat c " +
			"WHERE SIZE(c.users) = :size " +
			"AND :users MEMBER OF c.users")
	Boolean existsPrivateChatByUsers(@Param("users") Set<User> users, @Param("size") long size);
	
	@Query("SELECT c FROM Chat c " +
			"JOIN c.users u " +
			"WHERE c.eChatType = 'PRIVATE' " +
			"AND SIZE(c.users) = :size " +
			"AND u IN :users " +
			"GROUP BY c " +
			"HAVING COUNT(u) = :size")
	Optional<Chat> findPrivateChatByUsers(@Param("users") Set<User> users, @Param("size") long size);
	
	
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
			"FROM Chat c JOIN c.users u WHERE c.id = :chatId AND u.id = :userId")
	boolean isUserInChat(@Param("chatId") String chatId, @Param("userId") String userId);
	
	
	
}