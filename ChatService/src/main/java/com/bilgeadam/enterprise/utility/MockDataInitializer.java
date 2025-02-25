package com.bilgeadam.enterprise.utility;

import com.bilgeadam.enterprise.entity.*;
import com.bilgeadam.enterprise.repository.ChatRepository;
import com.bilgeadam.enterprise.repository.ChatUserRepository;
import com.bilgeadam.enterprise.repository.MessageRepository;
import com.bilgeadam.enterprise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MockDataInitializer implements ApplicationRunner {
	
	private final UserRepository userRepository;
	private final ChatRepository chatRepository;
	private final ChatUserRepository chatUserRepository;
	private final MessageRepository messageRepository;
	
	@Override
	public void run(ApplicationArguments args) {
		if (userRepository.findAll().stream().count() == 0 &&
		chatRepository.findAll().stream().count() == 0 &&
		chatUserRepository.findAll().stream().count() == 0 &&
		messageRepository.findAll().stream().count() == 0) {

			List<User> users = createMockUsers();
			userRepository.saveAll(users);
			List<Chat> chats = createMockChats();
			chatRepository.saveAll(chats);

			List<ChatUser> chatUsers = createMockChatUsers(chats, users);
			chatUserRepository.saveAll(chatUsers);

			List<Message> messages = createMockMessages(chats, chatUsers);
			messageRepository.saveAll(messages);
			System.out.println("Mock data initialized successfully!");
		}

		else System.out.println("Mock data not initialized!");
	}
	
	private List<User> createMockUsers() {
		List<User> users = new ArrayList<>();
		User vehbi = User.builder()
				.name("Vehbi")
				.surname("Çok")
				.email("vehbi@test.com")
				.isOnline(false)
				.password("Sifre123**")
				.build();

		User hasan = User.builder()
				.name("Hasan")
				.surname("Sanan")
				.email("hasan@test.com")
				.isOnline(true)
				.password("Sifre123**")
				.build();

		users.add(vehbi);
		users.add(hasan);
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
	
	private List<Chat> createMockChats() {
		List<Chat> chats = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			chats.add(Chat.builder()
			              .name("Chat " + i)
			              .description("This is description for Chat " + i)
			              .eChatType(i % 2 == 0 ? EChatType.PRIVATE : EChatType.GROUP)
			              .createDate(LocalDateTime.now().minusDays(i))
			              .isDeleted(false)
			              .build());
		}
		return chats;
	}
	
	private List<ChatUser> createMockChatUsers(List<Chat> chats, List<User> users) {
		List<ChatUser> chatUsers = new ArrayList<>();
		for (Chat chat : chats) {
			int numParticipants = chat.getEChatType() == EChatType.GROUP ? 3 : 2;
			for (int i = 0; i < numParticipants; i++) {
				chatUsers.add(ChatUser.builder()
				                      .chatId(chat.getId())
				                      .userId(users.get(i).getId())
				                      .build());
			}
		}
		return chatUsers;
	}
	
	private List<Message> createMockMessages(List<Chat> chats, List<ChatUser> chatUsers) {
		List<Message> messages = new ArrayList<>();
		for (Chat chat : chats) {
			for (int i = 1; i <= 5; i++) {
				String senderId = chatUsers.stream()
				                           .filter(cu -> cu.getChatId().equals(chat.getId()))
				                           .findFirst()
				                           .map(ChatUser::getUserId)
				                           .orElse(null);
				
				if (senderId != null) {
					messages.add(Message.builder()
					                    .content("Message " + i + " in chat " + chat.getName())
					                    .chatId(chat.getId())
					                    .senderId(senderId)
					                    .messageStatus(EMessageStatus.SENT)
					                    .timeStamp(LocalDateTime.now().minusMinutes(i))
					                    .isDeleted(false)
					                    .build());
				}
			}
		}
		return messages;
	}
}