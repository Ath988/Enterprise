package com.bilgeadam.enterprise.utility;

import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.MessageRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MockDataInitializer implements ApplicationRunner {
	
	private final UserRepository userRepository;
	private final ChatRepository chatRepository;
	private final MessageRepository messageRepository;
	
	@Override
	public void run(ApplicationArguments args) {
		List<User> users = createMockUsers();
		userRepository.saveAll(users);
		
		List<Chat> chats = createMockChats(users);
		chatRepository.saveAll(chats);
		
		List<Message> messages = createMockMessages(chats);
		messageRepository.saveAll(messages);
		
		System.out.println("Mock data initialized successfully!");
	}
	
	private List<User> createMockUsers() {
		List<User> users = new ArrayList<>();
		
		for (int i = 1; i <= 5; i++) {
			users.add(User.builder()
			              .email("user" + i + "@example.com")
			              .password("password" + i)
			              .name("User" + i)
			              .surname("Surname" + i)
			              .isOnline(i % 2 == 0)
			              .build());
		}
		
		return users;
	}
	
	private List<Chat> createMockChats(List<User> users) {
		List<Chat> chats = new ArrayList<>();
		
		for (int i = 1; i <= 3; i++) {
			Chat chat = Chat.builder()
			                .name("Chat " + i)
			                .description("This is description for Chat " + i)
			                .eChatType(i % 2 == 0 ? EChatType.PRIVATE : EChatType.GROUP)
			                .createDate(LocalDateTime.now().minusDays(i))
			                .isDeleted(false)
			                .build();

			Set<User> chatUsers = new HashSet<>(users.subList(0, i + 1));
			chat.setUsers(chatUsers);
			
			chats.add(chat);
		}
		
		return chats;
	}
	
	private List<Message> createMockMessages(List<Chat> chats) {
		List<Message> messages = new ArrayList<>();
		
		for (Chat chat : chats) {
			for (int i = 1; i <= 5; i++) {
				Message message = Message.builder()
				                         .content("Message " + i + " in chat " + chat.getName())
				                         .chat(chat)
				                         .sender(chat.getUsers().iterator().next())
				                         .messageStatus(EMessageStatus.SENT)
				                         .timeStamp(LocalDateTime.now().minusMinutes(i))
				                         .build();
				
				messages.add(message);
			}
		}
		
		return messages;
	}
}