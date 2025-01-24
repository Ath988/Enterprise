package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.view.ChatListView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,String> {
	
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
	
	
	
}